package ro.uaic.info.taskhandler.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ro.uaic.info.taskhandler.entity.Student;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.StudentRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.junit.jupiter.api.Test;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskStudentController.class)
class TaskStudentsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private StudentRepository studentRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createTaskStudent_BadRequest1() throws Exception {           //taskId == null
        Map<String, Integer> invalidTaskStudent1 = new HashMap<>();
        invalidTaskStudent1.put("studentId",0);
        invalidTaskStudent1.put("taskId",null);

        mockMvc.perform(post("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskStudent1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskStudent_BadRequest2() throws Exception{            //studentId == null
        Map<String, Integer> invalidTaskStudent2= new HashMap<>();
        invalidTaskStudent2.put("studentId",null);
        invalidTaskStudent2.put("taskId",0);

        mockMvc.perform(post("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskStudent2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskStudent_BadRequest3() throws Exception {           //taskOpt.isEmpty() == True
        Map<String, Integer> invalidTaskStudent1 = new HashMap<>();
        Integer studentId = 0;
        Integer taskId = 0;
        invalidTaskStudent1.put("studentId",studentId);
        invalidTaskStudent1.put("taskId",taskId);

        Student student = new Student();
        student.setStudentTasks(null);
        student.setName("student");
        student.setId(studentId);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));

        mockMvc.perform(post("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskStudent1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskStudent_BadRequest4() throws Exception {               //studentOpt.isEmpty() == True
        Map<String, Integer> invalidTaskStudent1 = new HashMap<>();
        Integer studentId = 0;
        Integer taskId = 0;
        invalidTaskStudent1.put("studentId",studentId);
        invalidTaskStudent1.put("taskId",taskId);

        Task task = new Task();
        task.setTaskStudents(null);
        task.setName("task");
        task.setId(studentId);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskStudent1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskStudent_Valid() throws Exception {
        Map<String, Integer> invalidTaskStudent1 = new HashMap<>();
        Integer studentId = 0;
        Integer taskId = 0;
        invalidTaskStudent1.put("studentId",studentId);
        invalidTaskStudent1.put("taskId",taskId);

        Task task = new Task();
        task.setName("task");
        task.setId(taskId);

        Student student = new Student();
        student.setName("student");
        student.setId(studentId);

        Set<Student> taskSet = new HashSet<>();
        taskSet.add(student);
        task.setTaskStudents(taskSet);

        Set<Task> studentSet = new HashSet<>();
        studentSet.add(task);
        student.setStudentTasks(studentSet);


        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskStudent1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }


    @Test
    void listByStudentId_NotFound() throws Exception {                                        //foundStudent.isEmpty() == True
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/task_student/student/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByStudentId_Valid() throws Exception {
        Student student = new Student();
        student.setName("student");
        student.setId(0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Set<Task> studentSet = new HashSet<>();
        studentSet.add(task);
        student.setStudentTasks(studentSet);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        mockMvc.perform(get("/task_student/student/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void listByTaskId_NotFound() throws Exception {                                                     //foundTask.isEmpty() == True
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/task_student/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByTaskId_Valid() throws Exception {
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Student student = new Student();
        student.setName("student");
        student.setId(0);

        Set<Student> taskSet = new HashSet<>();
        taskSet.add(student);
        task.setTaskStudents(taskSet);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        mockMvc.perform(get("/task_student/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void DeleteTaskStudent_BadRequest1() throws Exception {                           //taskOpt.isEmpty() == True

        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",null);
        map.put("taskId",0);

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskStudent_BadRequest2() throws Exception{                             //studentOpt.isEmpty() == True
        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",0);
        map.put("taskId",null);

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest());
    }

    //////////////////////////// DO BAD REQUEST 3,4

    @Test
    void DeleteTaskStudent_BadRequest3() throws Exception{
        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",0);
        map.put("taskId",0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Student student = new Student();
        student.setName("student");
        student.setId(0);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskStudent_BadRequest4() throws Exception{
        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",0);
        map.put("taskId",0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Student student = new Student();
        student.setName("student");
        student.setId(0);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskStudent_BadRequest5() throws Exception{                //task.getTaskStudents().contains(student) == False

        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",0);
        map.put("taskId",0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Student student = new Student();
        student.setName("student");
        student.setId(0);

        Set<Student> taskSet = new HashSet<>();
        task.setTaskStudents(taskSet);

        Set<Task> studentSet = new HashSet<>();
        studentSet.add(task);
        student.setStudentTasks(studentSet);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isNotFound());
    }

    @Test
    void DeleteTaskStudent_BadRequest6() throws Exception{                //student.getStudentTasks().contains(task) == False

        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",0);
        map.put("taskId",0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Student student = new Student();
        student.setName("student");
        student.setId(0);

        Set<Student> taskSet = new HashSet<>();
        taskSet.add(student);
        task.setTaskStudents(taskSet);

        Set<Task> studentSet = new HashSet<>();
        student.setStudentTasks(studentSet);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isNotFound());
    }

    ///////////////////// DO DELETE VALID

    @Test
    void DeleteTaskStudent_Valid() throws Exception{

        Map<String,Integer> map = new HashMap<>();
        map.put("studentId",0);
        map.put("taskId",0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Student student = new Student();
        student.setName("student");
        student.setId(0);

        Set<Student> taskSet = new HashSet<>();
        taskSet.add(student);
        task.setTaskStudents(taskSet);

        Set<Task> studentSet = new HashSet<>();
        studentSet.add(task);
        student.setStudentTasks(studentSet);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_student/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(map)))
                .andExpect(status().isNoContent());
    }
}