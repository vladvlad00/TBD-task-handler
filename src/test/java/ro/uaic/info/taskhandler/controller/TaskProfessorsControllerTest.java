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
import ro.uaic.info.taskhandler.entity.Professor;
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.ProfessorRepository;

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
@WebMvcTest(TaskProfessorsController.class)
class TaskProfessorsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private ProfessorRepository professorRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createTaskProfessor_BadRequest1() throws Exception {           //taskId == null
        Map<String, Integer> invalidTaskProfessor1 = new HashMap<>();
        invalidTaskProfessor1.put("professorId",0);
        invalidTaskProfessor1.put("taskId",null);

        mockMvc.perform(post("/task_professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskProfessor1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskProfessor_BadRequest2() throws Exception{            //professorId == null
        Map<String, Integer> invalidTaskProfessor2= new HashMap<>();
        invalidTaskProfessor2.put("professorId",null);
        invalidTaskProfessor2.put("taskId",0);

        mockMvc.perform(post("/task_professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskProfessor2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskProfessor_BadRequest3() throws Exception {           //taskOpt.isEmpty() == True
        Map<String, Integer> invalidTaskProfessor1 = new HashMap<>();
        Integer professorId = 0;
        Integer taskId = 0;
        invalidTaskProfessor1.put("professorId",professorId);
        invalidTaskProfessor1.put("taskId",taskId);

        Professor professor = new Professor();
        professor.setProfessorTasks(null);
        professor.setName("professor");
        professor.setId(professorId);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));

        mockMvc.perform(post("/task_professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskProfessor1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskProfessor_BadRequest4() throws Exception {               //professorOpt.isEmpty() == True
        Map<String, Integer> invalidTaskProfessor1 = new HashMap<>();
        Integer professorId = 0;
        Integer taskId = 0;
        invalidTaskProfessor1.put("professorId",professorId);
        invalidTaskProfessor1.put("taskId",taskId);

        Task task = new Task();
        task.setTaskProfessors(null);
        task.setName("task");
        task.setId(professorId);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/task_professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskProfessor1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskProfessor_Valid() throws Exception {
        Map<String, Integer> invalidTaskProfessor1 = new HashMap<>();
        Integer professorId = 0;
        Integer taskId = 0;
        invalidTaskProfessor1.put("professorId",professorId);
        invalidTaskProfessor1.put("taskId",taskId);

        Task task = new Task();
        task.setName("task");
        task.setId(taskId);

        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(professorId);

        Set<Professor> taskSet = new HashSet<>();
        taskSet.add(professor);
        task.setTaskProfessors(taskSet);

        Set<Task> professorSet = new HashSet<>();
        professorSet.add(task);
        professor.setProfessorTasks(professorSet);


        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));
        when(taskRepository.save(any(Task.class))).thenReturn(task);
        when(professorRepository.save(any(Professor.class))).thenReturn(professor);

        mockMvc.perform(post("/task_professor/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskProfessor1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").exists());
    }


    @Test
    void listByProfessorId_NotFound() throws Exception {                                        //foundProfessor.isEmpty() == True
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/task_professor/professor/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByProfessorId_Valid() throws Exception {
        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Set<Task> professorSet = new HashSet<>();
        professorSet.add(task);
        professor.setProfessorTasks(professorSet);

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));
        mockMvc.perform(get("/task_professor/professor/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void listByTaskId_NotFound() throws Exception {                                                     //foundTask.isEmpty() == True
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/task_professor/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByTaskId_Valid() throws Exception {
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(0);

        Set<Professor> taskSet = new HashSet<>();
        taskSet.add(professor);
        task.setTaskProfessors(taskSet);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        mockMvc.perform(get("/task_professor/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void DeleteTaskProfessor_BadRequest1() throws Exception {                           //taskOpt.isEmpty() == True

        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(0);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));

        mockMvc.perform(delete("/task_professor/0/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskProfessor_BadRequest2() throws Exception{                             //professorOpt.isEmpty() == True
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_professor/0/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskProfessor_BadRequest3() throws Exception{                //task.getTaskProfessors().contains(professor) == False
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(0);

        Set<Professor> taskSet = new HashSet<>();
        task.setTaskProfessors(taskSet);

        Set<Task> professorSet = new HashSet<>();
        professorSet.add(task);
        professor.setProfessorTasks(professorSet);

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_professor/0/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void DeleteTaskProfessor_BadRequest4() throws Exception{                //professor.getProfessorTasks().contains(task) == False
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(0);

        Set<Professor> taskSet = new HashSet<>();
        taskSet.add(professor);
        task.setTaskProfessors(taskSet);

        Set<Task> professorSet = new HashSet<>();
        professor.setProfessorTasks(professorSet);

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_professor/0/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void DeleteTaskProfessor_Valid() throws Exception{
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Professor professor = new Professor();
        professor.setName("professor");
        professor.setId(0);

        Set<Professor> taskSet = new HashSet<>();
        taskSet.add(professor);
        task.setTaskProfessors(taskSet);

        Set<Task> professorSet = new HashSet<>();
        professorSet.add(task);
        professor.setProfessorTasks(professorSet);

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_professor/0/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}