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
import ro.uaic.info.taskhandler.repository.ProfessorRepository;

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
@WebMvcTest(ProfessorController.class)
class ProfessorControllerTest {

    @MockBean
    ProfessorRepository professorRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createProfessor_BadRequest() throws Exception {

        Professor testInvalidProfessor = new Professor();
        testInvalidProfessor.setProfessorTasks(null);
        testInvalidProfessor.setName("testInvalidProfessor");
        testInvalidProfessor.setId(0);
        when(professorRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(testInvalidProfessor));

        mockMvc.perform(post("/professor/")
                .contentType(mapper.writeValueAsString(testInvalidProfessor))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// bad request case - testInvalidProfessor.id != null && and professor already exists /////////////////////////////////////////////////////////////////////



    }

    @Test
    void createProfessor_Valid() throws Exception{
        Professor testValidProfessor = new Professor();
        testValidProfessor.setProfessorTasks(null);
        testValidProfessor.setName("testProfessor");
        testValidProfessor.setId(null);
        when(professorRepository.save(any(Professor.class))).thenReturn(testValidProfessor);
        mockMvc.perform(post("/professor/")
                .content(mapper.writeValueAsString(testValidProfessor))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(testValidProfessor.getName()));
        ///////////////////////////////////////////////////////////////////// case in which we successfully create a professor /////////////////////////////////////////////////////////////////////
    }

    @Test
    void listAllProfessors_NotFound() throws Exception {
        List<Professor> foundProfessors1 = null;
        when(professorRepository.findAll()).thenReturn(foundProfessors1);

        mockMvc.perform(get("/professor/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case /////////////////////////////////////////////////////////////////////

    }

    @Test
    void listAllProfessors_Valid() throws Exception {
        Professor testValidProfessor1 = new Professor();
        testValidProfessor1.setProfessorTasks(null);
        testValidProfessor1.setName("testValidProfessor1");

        Professor testValidProfessor2 = new Professor();
        testValidProfessor2.setProfessorTasks(null);
        testValidProfessor2.setName("testValidProfessor2");

        List<Professor> foundProfessors2 = new ArrayList<>();

        foundProfessors2.add(testValidProfessor1);
        foundProfessors2.add(testValidProfessor2);

        when(professorRepository.findAll()).thenReturn(foundProfessors2);

        mockMvc.perform(get("/professor/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        ///////////////////////////////////////////////////////////////////// case in which we successfully retrieve all professors /////////////////////////////////////////////////////////////////////
    }

    @Test
    void listProfessor_NotFound() throws Exception {

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/professor/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case /////////////////////////////////////////////////////////////////////

    }

    @Test
    void listProfessor_Valid() throws Exception {
        Professor testProfessor = new Professor();
        testProfessor.setProfessorTasks(null);
        testProfessor.setName("testProfessor");
        testProfessor.setId(0);

        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(testProfessor));

        mockMvc.perform(get("/professor/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        ///////////////////////////////////////////////////////////////////// case in which we successfully find the professor /////////////////////////////////////////////////////////////////////
    }

    @Test
    void updateProfessor_BadRequest1() throws Exception {
        Professor testInvalidProfessor1 = new Professor();
        testInvalidProfessor1.setId(null);
        testInvalidProfessor1.setProfessorTasks(null);
        testInvalidProfessor1.setName("testInvalidProfessor1");

        mockMvc.perform(put("/professor/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidProfessor1)))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// case 1 of bad request - testInvalidProfessor1.id == null /////////////////////////////////////////////////////////////////////

    }

    @Test
    void updateProfessor_BadRequest2() throws Exception {
        Professor testInvalidProfessor2 = new Professor();
        testInvalidProfessor2.setId(1);
        testInvalidProfessor2.setProfessorTasks(null);
        testInvalidProfessor2.setName("testInvalidProfessor2");

        mockMvc.perform(put("/professor/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidProfessor2)))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// case 2 of bad request - testInvalidProfessor2.id != {id} /////////////////////////////////////////////////////////////////////

    }

    @Test
    void updateProfessor_NotFound() throws Exception {
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Professor testInvalidProfessor3 = new Professor();
        testInvalidProfessor3.setId(0);
        testInvalidProfessor3.setProfessorTasks(null);
        testInvalidProfessor3.setName("testInvalidProfessor3");

        mockMvc.perform(put("/professor/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidProfessor3)))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case - couldn't find a professor with the given id to update /////////////////////////////////////////////////////////////////////


    }

    @Test
    void updateProfessor_Valid() throws Exception {
        Professor testValidProfessor = new Professor();
        testValidProfessor.setId(0);
        testValidProfessor.setProfessorTasks(null);
        testValidProfessor.setName("testValidProfessor");
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidProfessor));
        when(professorRepository.save(any(Professor.class))).thenReturn(testValidProfessor);
        mockMvc.perform(put("/professor/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testValidProfessor)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testValidProfessor.getName()))
                .andExpect(jsonPath("$.id").value(testValidProfessor.getId()));
        ///////////////////////////////////////////////////////////////////// case in which we successfully update the professor with the given id /////////////////////////////////////////////////////////////////////
    }

    @Test
    void deleteProfessor_NotFound() throws Exception {
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/professor/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case - couldn't find a professor with the given id to delete /////////////////////////////////////////////////////////////////////

    }

    @Test
    void deleteProfessor_Valid() throws Exception {
        Professor testValidProfessor = new Professor();
        testValidProfessor.setId(0);
        testValidProfessor.setProfessorTasks(null);
        testValidProfessor.setName("testValidProfessor");
        when(professorRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidProfessor));
        mockMvc.perform(delete("/professor/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ///////////////////////////////////////////////////////////////////// case in which we successfully delete the professor with the given id /////////////////////////////////////////////////////////////////////
    }
}
