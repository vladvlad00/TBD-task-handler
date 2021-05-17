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
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @MockBean
    TaskRepository taskRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createTask_BadRequest() throws Exception {

        Task testInvalidTask = new Task();
        testInvalidTask.setTaskProfessors(null);
        testInvalidTask.setTaskQuestions(null);
        testInvalidTask.setName("testInvalidTask");
        testInvalidTask.setId(0);
        when(taskRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(testInvalidTask));

        mockMvc.perform(post("/task/")
                .contentType(mapper.writeValueAsString(testInvalidTask))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// bad request case - testInvalidTask.id != null && and task already exists /////////////////////////////////////////////////////////////////////



    }

    @Test
    void createTask_Valid() throws Exception{
        Task testValidTask = new Task();
        testValidTask.setTaskProfessors(null);
        testValidTask.setTaskQuestions(null);
        testValidTask.setName("testTask");
        testValidTask.setId(null);
        when(taskRepository.save(any(Task.class))).thenReturn(testValidTask);
        mockMvc.perform(post("/task/")
                .content(mapper.writeValueAsString(testValidTask))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(testValidTask.getName()));
        ///////////////////////////////////////////////////////////////////// case in which we successfully create a task /////////////////////////////////////////////////////////////////////
    }

//    @Test
//    void listAllTasks_NotFound() throws Exception {
//        List<Task> foundTasks1 = null;
//        when(taskRepository.findAll()).thenReturn(foundTasks1);
//
//        mockMvc.perform(get("/task/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//        ///////////////////////////////////////////////////////////////////// status not found case /////////////////////////////////////////////////////////////////////
//
//    }

    @Test
    void listAllTasks_Valid() throws Exception {
        Task testValidTask1 = new Task();
        testValidTask1.setTaskProfessors(null);
        testValidTask1.setTaskQuestions(null);
        testValidTask1.setName("testValidTask1");

        Task testValidTask2 = new Task();
        testValidTask2.setTaskProfessors(null);
        testValidTask2.setName("testValidTask2");

        List<Task> foundTasks2 = new ArrayList<>();

        foundTasks2.add(testValidTask1);
        foundTasks2.add(testValidTask2);

        when(taskRepository.findAll()).thenReturn(foundTasks2);

        mockMvc.perform(get("/task/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        ///////////////////////////////////////////////////////////////////// case in which we successfully retrieve all tasks /////////////////////////////////////////////////////////////////////
    }

    @Test
    void listTask_NotFound() throws Exception {

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case /////////////////////////////////////////////////////////////////////

    }

    @Test
    void listTask_Valid() throws Exception {
        Task testTask = new Task();
        testTask.setTaskProfessors(null);
        testTask.setTaskQuestions(null);
        testTask.setName("testTask");
        testTask.setId(0);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(testTask));

        mockMvc.perform(get("/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        ///////////////////////////////////////////////////////////////////// case in which we successfully find the task /////////////////////////////////////////////////////////////////////
    }

    @Test
    void updateTask_BadRequest1() throws Exception {
        Task testInvalidTask1 = new Task();
        testInvalidTask1.setId(null);
        testInvalidTask1.setTaskProfessors(null);
        testInvalidTask1.setTaskQuestions(null);
        testInvalidTask1.setName("testInvalidTask1");

        mockMvc.perform(put("/task/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidTask1)))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// case 1 of bad request - testInvalidTask1.id == null /////////////////////////////////////////////////////////////////////

    }

    @Test
    void updateTask_BadRequest2() throws Exception {
        Task testInvalidTask2 = new Task();
        testInvalidTask2.setId(1);
        testInvalidTask2.setTaskProfessors(null);
        testInvalidTask2.setTaskQuestions(null);
        testInvalidTask2.setName("testInvalidTask2");

        mockMvc.perform(put("/task/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidTask2)))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// case 2 of bad request - testInvalidTask2.id != {id} /////////////////////////////////////////////////////////////////////

    }

    @Test
    void updateTask_NotFound() throws Exception {
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Task testInvalidTask3 = new Task();
        testInvalidTask3.setId(0);
        testInvalidTask3.setTaskProfessors(null);
        testInvalidTask3.setTaskQuestions(null);
        testInvalidTask3.setName("testInvalidTask3");
        mockMvc.perform(put("/task/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidTask3)))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case - couldn't find a task with the given id to update /////////////////////////////////////////////////////////////////////


    }

    @Test
    void updateTask_Valid() throws Exception {
        Task testValidTask = new Task();
        testValidTask.setId(0);
        testValidTask.setTaskProfessors(null);
        testValidTask.setTaskQuestions(null);
        testValidTask.setName("testValidTask");
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidTask));
        when(taskRepository.save(any(Task.class))).thenReturn(testValidTask);
        mockMvc.perform(put("/task/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testValidTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testValidTask.getName()))
                .andExpect(jsonPath("$.id").value(testValidTask.getId()));
        ///////////////////////////////////////////////////////////////////// case in which we successfully update the task with the given id /////////////////////////////////////////////////////////////////////
    }

    @Test
    void deleteTask_NotFound() throws Exception {
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case - couldn't find a task with the given id to delete /////////////////////////////////////////////////////////////////////

    }

    @Test
    void deleteTask_Valid() throws Exception {
        Task testValidTask = new Task();
        testValidTask.setId(0);
        testValidTask.setTaskProfessors(null);
        testValidTask.setTaskQuestions(null);

        testValidTask.setName("testValidTask");
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidTask));
        mockMvc.perform(delete("/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ///////////////////////////////////////////////////////////////////// case in which we successfully delete the task with the given id /////////////////////////////////////////////////////////////////////
    }
}