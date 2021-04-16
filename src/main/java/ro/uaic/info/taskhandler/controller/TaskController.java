package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path="task")
public class TaskController
{
    @Autowired
    private TaskRepository taskRepository;

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
        if (foundTasks == null)
            return ResponseEntity.notFound().build();
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

        if (updatedTask == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Integer id)
    {
        if (taskRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        taskRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
