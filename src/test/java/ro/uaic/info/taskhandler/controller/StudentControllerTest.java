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
import ro.uaic.info.taskhandler.repository.StudentRepository;

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
@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @MockBean
    StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createStudent_BadRequest() throws Exception {

        Student testInvalidStudent = new Student();
        testInvalidStudent.setName("testInvalidStudent");
        testInvalidStudent.setId(0);
        when(studentRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(testInvalidStudent));

        mockMvc.perform(post("/student/")
                .contentType(mapper.writeValueAsString(testInvalidStudent))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        // bad request case - testInvalidStudent.id != null && and student already exists //



    }

    @Test
    void createStudent_Valid() throws Exception{
        Student testValidStudent = new Student();
        testValidStudent.setName("testStudent");
        testValidStudent.setId(null);
        when(studentRepository.save(any(Student.class))).thenReturn(testValidStudent);
        mockMvc.perform(post("/student/")
                .content(mapper.writeValueAsString(testValidStudent))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(testValidStudent.getName()));
        // case in which we successfully create a student //
    }

//    @Test
//    void listAllStudents_NotFound() throws Exception {
//        List<Student> foundStudents1 = null;
//        when(studentRepository.findAll()).thenReturn(foundStudents1);
//
//        mockMvc.perform(get("/student/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//        // status not found case //
//
//    }

    @Test
    void listAllStudents_Valid() throws Exception {
        Student testValidStudent1 = new Student();
        testValidStudent1.setName("testValidStudent1");

        Student testValidStudent2 = new Student();
        testValidStudent2.setName("testValidStudent2");

        List<Student> foundStudents2 = new ArrayList<>();

        foundStudents2.add(testValidStudent1);
        foundStudents2.add(testValidStudent2);

        when(studentRepository.findAll()).thenReturn(foundStudents2);

        mockMvc.perform(get("/student/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        // case in which we successfully retrieve all students //
    }

    @Test
    void listStudent_NotFound() throws Exception {

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/student/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // status not found case //

    }

    @Test
    void listStudent_Valid() throws Exception {
        Student testStudent = new Student();
        testStudent.setName("testStudent");
        testStudent.setId(0);

        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(testStudent));

        mockMvc.perform(get("/student/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        // case in which we successfully find the student //
    }

    @Test
    void updateStudent_BadRequest1() throws Exception {
        Student testInvalidStudent1 = new Student();
        testInvalidStudent1.setId(null);
        testInvalidStudent1.setName("testInvalidStudent1");

        mockMvc.perform(put("/student/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidStudent1)))
                .andExpect(status().isBadRequest());
        // case 1 of bad request - testInvalidStudent1.id == null //

    }

    @Test
    void updateStudent_BadRequest2() throws Exception {
        Student testInvalidStudent2 = new Student();
        testInvalidStudent2.setId(1);
        testInvalidStudent2.setName("testInvalidStudent2");

        mockMvc.perform(put("/student/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidStudent2)))
                .andExpect(status().isBadRequest());
        // case 2 of bad request - testInvalidStudent2.id != {id} //

    }

    @Test
    void updateStudent_NotFound() throws Exception {
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        Student testInvalidStudent3 = new Student();
        testInvalidStudent3.setId(0);
        testInvalidStudent3.setName("testInvalidStudent3");

        mockMvc.perform(put("/student/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidStudent3)))
                .andExpect(status().isNotFound());
        // status not found case - couldn't find a student with the given id to update //


    }

    @Test
    void updateStudent_Valid() throws Exception {
        Student testValidStudent = new Student();
        testValidStudent.setId(0);
        testValidStudent.setName("testValidStudent");
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testValidStudent);
        mockMvc.perform(put("/student/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testValidStudent)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(testValidStudent.getName()))
                .andExpect(jsonPath("$.id").value(testValidStudent.getId()));
        // case in which we successfully update the student with the given id //
    }

    @Test
    void deleteStudent_NotFound() throws Exception {
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/student/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        // status not found case - couldn't find a student with the given id to delete //

    }

    @Test
    void deleteStudent_Valid() throws Exception {
        Student testValidStudent = new Student();
        testValidStudent.setId(0);
        testValidStudent.setName("testValidStudent");
        when(studentRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidStudent));
        mockMvc.perform(delete("/student/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        // case in which we successfully delete the student with the given id //
    }
}
