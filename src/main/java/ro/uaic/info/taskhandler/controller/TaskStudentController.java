package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Student;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.StudentRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/task_student")
public class TaskStudentController {
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    @PostMapping("/")
    public ResponseEntity<Map<String, Integer>> createTaskStudent(@RequestBody Map<String, Integer> taskStudent)
    {
        Integer studentId = taskStudent.get("studentId");
        Integer taskId = taskStudent.get("taskId");

        if (taskId == null || studentId == null)
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Student> studentOpt =  studentRepository.findById(studentId);

        if (taskOpt.isEmpty() || studentOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Student student = studentOpt.get();

        task.getTaskStudents().add(student);
        student.getStudentTasks().add(task);

        taskRepository.save(task);
        studentRepository.save(student);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{taskId}/{studentId}").buildAndExpand(taskId, studentId).toUri();
        return ResponseEntity.created(uri).body(taskStudent);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<Iterable<Task>> listByStudentId(@PathVariable Integer id)
    {
        Optional<Student> foundStudent = studentRepository.findById(id);
        if (foundStudent.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundStudent.get().getStudentTasks());
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<Iterable<Student>> listByTaskId(@PathVariable Integer id)
    {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(foundTask.get().getTaskStudents());
    }

    @DeleteMapping("/")
    public ResponseEntity<Task> deleteTask(@RequestBody Map<String, Integer> taskStudent)
    {
        Integer studentId = taskStudent.get("studentId");
        Integer taskId = taskStudent.get("taskId");

        if (taskId == null || studentId == null)
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Student> studentOpt =  studentRepository.findById(studentId);

        if (taskOpt.isEmpty() || studentOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Student student = studentOpt.get();

        if (!task.getTaskStudents().contains(student) || !student.getStudentTasks().contains(task))
            return ResponseEntity.notFound().build();

        task.getTaskStudents().remove(student);
        student.getStudentTasks().remove(task);

        taskRepository.save(task);
        studentRepository.save(student);

        return ResponseEntity.noContent().build();
    }
}
