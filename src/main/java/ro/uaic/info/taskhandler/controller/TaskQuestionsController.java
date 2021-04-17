package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Question;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.QuestionRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/task_question")
public class TaskQuestionsController
{
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/")
    public ResponseEntity<Map<String, Integer>> createTaskQuestion(@RequestBody Map<String, Integer> taskQuestion)
    {
        Integer questionId = taskQuestion.get("questionId");
        Integer taskId = taskQuestion.get("taskId");

        if (taskId == null || questionId == null)
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Question> questionOpt =  questionRepository.findById(questionId);

        if (taskOpt.isEmpty() || questionOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Question question = questionOpt.get();

        task.getTaskQuestions().add(question);
        question.getQuestionTasks().add(task);

        taskRepository.save(task);
        questionRepository.save(question);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{taskId}/{questionId}").buildAndExpand(taskId, questionId).toUri();
        return ResponseEntity.created(uri).body(taskQuestion);
    }

    @GetMapping("/question/{id}")
    public ResponseEntity<Iterable<Task>> listByQuestionId(@PathVariable Integer id)
    {
        Optional<Question> foundQuestion = questionRepository.findById(id);
        if (foundQuestion.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundQuestion.get().getQuestionTasks());
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Iterable<Question>> listByTaskId(@PathVariable Integer id)
    {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundTask.get().getTaskQuestions());
    }

    @DeleteMapping("/")
    public ResponseEntity<Task> deleteTask(@RequestBody Map<String, Integer> taskQuestion)
    {
        Integer questionId = taskQuestion.get("questionId");
        Integer taskId = taskQuestion.get("taskId");

        if (taskId == null || questionId == null)
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Question> questionOpt =  questionRepository.findById(questionId);

        if (taskOpt.isEmpty() || questionOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Question question = questionOpt.get();

        if (!task.getTaskQuestions().contains(question) || !question.getQuestionTasks().contains(task))
            return ResponseEntity.notFound().build();

        task.getTaskQuestions().remove(question);
        question.getQuestionTasks().remove(task);

        taskRepository.save(task);
        questionRepository.save(question);

        return ResponseEntity.noContent().build();
    }
}
