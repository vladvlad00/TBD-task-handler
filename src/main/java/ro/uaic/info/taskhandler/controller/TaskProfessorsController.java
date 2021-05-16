package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Professor;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.ProfessorRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/task_professor")
public class TaskProfessorsController
{
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping("/")
    public ResponseEntity<Map<String, Integer>> createTaskProfessor(@RequestBody Map<String, Integer> taskProfessor)
    {
        Integer professorId = taskProfessor.get("professorId");
        Integer taskId = taskProfessor.get("taskId");

        if (taskId == null || professorId == null)
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Professor> professorOpt = professorRepository.findById(professorId);

        if (taskOpt.isEmpty() || professorOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Professor professor = professorOpt.get();

        task.getTaskProfessors().add(professor);
        professor.getProfessorTasks().add(task);

        taskRepository.save(task);
        professorRepository.save(professor);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{taskId}/{professorId}").buildAndExpand(taskId, professorId).toUri();
        return ResponseEntity.created(uri).body(taskProfessor);
    }

    @GetMapping("/professor/{id}")
    public ResponseEntity<Iterable<Task>> listByProfessorId(@PathVariable Integer id)
    {
        Optional<Professor> foundProfessor = professorRepository.findById(id);
        if (foundProfessor.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundProfessor.get().getProfessorTasks());
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Iterable<Professor>> listByTaskId(@PathVariable Integer id)
    {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundTask.get().getTaskProfessors());
    }

    @DeleteMapping("/task/{taskId}/professor/{professorId}")
    public ResponseEntity<Task> deleteTask(@PathVariable Integer taskId, @PathVariable Integer professorId)
    {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Professor> professorOpt = professorRepository.findById(professorId);

        if (taskOpt.isEmpty() || professorOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Professor professor = professorOpt.get();

        if (!task.getTaskProfessors().contains(professor) || !professor.getProfessorTasks().contains(task))
            return ResponseEntity.notFound().build();

        task.getTaskProfessors().remove(professor);
        professor.getProfessorTasks().remove(task);

        taskRepository.save(task);
        professorRepository.save(professor);

        return ResponseEntity.noContent().build();
    }
}
