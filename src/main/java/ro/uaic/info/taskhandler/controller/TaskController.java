package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.AnswerRepository;
import ro.uaic.info.taskhandler.repository.ScoreAnswerRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path="task")
public class TaskController
{
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ScoreAnswerRepository scoreAnswerRepository;

    @PostMapping("/")
    public ResponseEntity<Task> createTask(@RequestBody Task task)
    {
        if (task.getId() != null && taskRepository.findById(task.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Task createdTask = taskRepository.save(task);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdTask.getId()).toUri();
        return ResponseEntity.created(uri).body(createdTask);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Task>> listAllTasks()
    {
        Iterable<Task> foundTasks = taskRepository.findAll();
        return ResponseEntity.ok(foundTasks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> listTask(@PathVariable Integer id)
    {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundTask.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@RequestBody Task task, @PathVariable Integer id)
    {
        if (task.getId() == null || !task.getId().equals(id))
            return ResponseEntity.badRequest().build();
        if (taskRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Task updatedTask = taskRepository.save(task);

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Integer id)
    {
        Optional<Task> taskOpt = taskRepository.findById(id);

        if (taskOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Task taskObj = taskOpt.get();

        if (taskObj.getTaskProfessors() != null)
        {
            for (var professor : taskObj.getTaskProfessors())
                professor.getProfessorTasks().remove(taskObj);
        }

        if (taskObj.getAnswers() != null)
        {
            for (var answer : taskObj.getAnswers())
                answerRepository.deleteById(answer.getId());
        }

        if (taskObj.getScoreAnswers() != null)
        {
            for (var score : taskObj.getScoreAnswers())
                scoreAnswerRepository.deleteById(score.getId());
        }

        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
