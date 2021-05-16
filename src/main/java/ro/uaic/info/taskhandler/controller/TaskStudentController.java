package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.*;
import ro.uaic.info.taskhandler.repository.StudentRepository;
import ro.uaic.info.taskhandler.repository.TaskRegisterRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.*;

@RestController
@RequestMapping("/task_student")
public class TaskStudentController
{
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TaskRegisterRepository taskRegisterRepository;

    @PostMapping("/")
    public ResponseEntity<TaskRegistration> createTaskStudent(@RequestBody Map<String, Integer> taskStudent)
    {
        Integer studentId = taskStudent.get("studentId");
        Integer taskId = taskStudent.get("taskId");

        if (taskId == null || studentId == null)
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (taskOpt.isEmpty() || studentOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Task task = taskOpt.get();
        Student student = studentOpt.get();

        var id = new TaskRegistrationPK(studentId, taskId);
        var register = new TaskRegistration(id, student, task, "not started");

        task.getTaskStudents().add(register);
        student.getStudentTasks().add(register);

        taskRegisterRepository.save(register);
        taskRepository.save(task);
        studentRepository.save(student);


        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{taskId}/{studentId}").buildAndExpand(taskId, studentId).toUri();
        return ResponseEntity.created(uri).body(register);
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<List<Map<String, Object>>> listByStudentId(@PathVariable Integer id)
    {
        Optional<Student> foundStudent = studentRepository.findById(id);
        if (foundStudent.isEmpty())
            return ResponseEntity.notFound().build();

        var aux = foundStudent.get().getStudentTasks();
        var ans = new ArrayList<Map<String, Object>>();
        for (var element : aux)
        {
            var map = new HashMap<String, Object>();
            map.put("task", element.getTask());
            map.put("status", element.getStatus());
            ans.add(map);
        }
        return ResponseEntity.ok(ans);
    }

    @GetMapping("/task/{id}")
    public ResponseEntity<List<Map<String, Object>>> listByTaskId(@PathVariable Integer id)
    {
        Optional<Task> foundTask = taskRepository.findById(id);
        if (foundTask.isEmpty())
            return ResponseEntity.notFound().build();

        var aux = foundTask.get().getTaskStudents();
        var ans = new ArrayList<Map<String, Object>>();
        for (var element : aux)
        {
            var map = new HashMap<String, Object>();
            map.put("student", element.getStudent());
            map.put("status", element.getStatus());
            ans.add(map);
        }
        return ResponseEntity.ok(ans);
    }

    @GetMapping("/task/{taskId}/student/{studentId}")
    public ResponseEntity<TaskRegistration> listTaskAssignedToStudent(@PathVariable Integer taskId, @PathVariable Integer studentId)
    {
        Optional<TaskRegistration> found = taskRegisterRepository.findByTaskIdAndStudentId(taskId, studentId);
        if (found.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(found.get());
    }

    @PutMapping("/task/{taskId}/student/{studentId}")
    public ResponseEntity<TaskRegistration> updateTask(@PathVariable Integer taskId, @PathVariable Integer studentId)
    {

        Optional<TaskRegistration> found = taskRegisterRepository.findByTaskIdAndStudentId(taskId, studentId);
        if (found.isEmpty())
            return ResponseEntity.notFound().build();

        if (found.get().getStatus().equals("not started"))
        {
            found.get().setStatus("started");
            TaskRegistration updatedTask = taskRegisterRepository.save(found.get());
            return ResponseEntity.ok(updatedTask);
        } else
            return ResponseEntity.badRequest().build();

    }


    @DeleteMapping("/task/{taskId}/student/{studentId}")
    public ResponseEntity<Task> deleteTask(@PathVariable Integer taskId, @PathVariable Integer studentId)
    {

        if (taskId == null || studentId == null)
            return ResponseEntity.badRequest().build();

        Optional<TaskRegistration> taskRegistrationOpt = taskRegisterRepository.findById(new TaskRegistrationPK(studentId, taskId));

        if (taskRegistrationOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Optional<Task> taskOpt = taskRepository.findById(taskId);
        Optional<Student> studentOpt = studentRepository.findById(studentId);

        if (taskOpt.isEmpty() || studentOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        TaskRegistration taskRegistration = taskRegistrationOpt.get();
        Task task = taskOpt.get();
        Student student = studentOpt.get();

        if (!task.getTaskStudents().contains(taskRegistration) || !student.getStudentTasks().contains(taskRegistration))
            return ResponseEntity.notFound().build();

        task.getTaskStudents().remove(taskRegistration);
        student.getStudentTasks().remove(taskRegistration);

        taskRepository.save(task);
        studentRepository.save(student);

        taskRegisterRepository.deleteById(new TaskRegistrationPK(studentId, taskId));

        return ResponseEntity.noContent().build();
    }
}
