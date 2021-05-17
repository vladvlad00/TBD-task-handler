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
import ro.uaic.info.taskhandler.entity.Question;
import ro.uaic.info.taskhandler.repository.QuestionRepository;

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
@WebMvcTest(QuestionController.class)
class QuestionControllerTest {

    @MockBean
    QuestionRepository questionRepository;

    @Autowired
    private MockMvc mockMvc;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createQuestion_BadRequest() throws Exception {

        Question testInvalidQuestion = new Question();
        testInvalidQuestion.setQuestionTasks(null);
        testInvalidQuestion.setContent("testInvalidQuestion");
        testInvalidQuestion.setId(0);
        when(questionRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(testInvalidQuestion));

        mockMvc.perform(post("/question/")
                .contentType(mapper.writeValueAsString(testInvalidQuestion))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// bad request case - testInvalidQuestion.id != null && and question already exists /////////////////////////////////////////////////////////////////////



    }

    @Test
    void createQuestion_Valid() throws Exception{
        Question testValidQuestion = new Question();
        testValidQuestion.setQuestionTasks(null);
        testValidQuestion.setContent("testQuestion");
        testValidQuestion.setId(null);
        when(questionRepository.save(any(Question.class))).thenReturn(testValidQuestion);
        mockMvc.perform(post("/question/")
                .content(mapper.writeValueAsString(testValidQuestion))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(testValidQuestion.getContent()));
        ///////////////////////////////////////////////////////////////////// case in which we successfully create a question /////////////////////////////////////////////////////////////////////
    }
    
    @Test
    void createQuestions_BadRequest() throws Exception {
        List<Question> testInvalidQuestions = new ArrayList<>();
        Question testInvalidQuestionsInstance;

        testInvalidQuestionsInstance = new Question();
        testInvalidQuestionsInstance.setQuestionTasks(null);
        testInvalidQuestionsInstance.setContent(new String("testQuestion"));
        testInvalidQuestionsInstance.setId(null);
        testInvalidQuestions.add(testInvalidQuestionsInstance);

        testInvalidQuestionsInstance = new Question();
        testInvalidQuestionsInstance.setQuestionTasks(null);
        testInvalidQuestionsInstance.setContent("testInvalidQuestion");
        testInvalidQuestionsInstance.setId(1);
        testInvalidQuestions.add(testInvalidQuestionsInstance);

        when(questionRepository.findById(any(Integer.class))).thenReturn(java.util.Optional.of(testInvalidQuestionsInstance));

        mockMvc.perform(post("/question/bulk/")
                .contentType(mapper.writeValueAsString(testInvalidQuestions))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// bad request case - testInvalidQuestionsInstance.get().id != null && and question already exists /////////////////////////////////////////////////////////////////////
    }

    @Test
    void createQuestions_Valid() throws Exception {
        List<Question> testValidQuestions = new ArrayList<>();
        Question testValidQuestionsInstance;

        /*testValidQuestionsInstance = new Question();
        testValidQuestionsInstance.setQuestionTasks(null);
        testValidQuestionsInstance.setContent(new String("testQuestion1"));
        testValidQuestionsInstance.setId(null);
        testValidQuestions.add(testValidQuestionsInstance);*/

        testValidQuestionsInstance = new Question();
        testValidQuestionsInstance.setQuestionTasks(null);
        testValidQuestionsInstance.setContent(new String("testQuestion2"));
        testValidQuestionsInstance.setId(null);
        testValidQuestions.add(testValidQuestionsInstance);

        when(questionRepository.save(any(Question.class))).thenReturn(testValidQuestionsInstance);
        mockMvc.perform(post("/question/bulk/")
                .content(mapper.writeValueAsString(testValidQuestions))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].body.content").value(testValidQuestions.get(0).getContent()))
                /*.andExpect(jsonPath("$[1].body.content").value(testValidQuestions.get(1).getContent()))*/;
        ///////////////////////////////////////////////////////////////////// case in which we successfully create questions /////////////////////////////////////////////////////////////////////
    }

//    @Test
//    void listAllQuestions_NotFound() throws Exception {
//        List<Question> foundQuestions1 = null;
//        when(questionRepository.findAll()).thenReturn(foundQuestions1);
//
//        mockMvc.perform(get("/question/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//        ///////////////////////////////////////////////////////////////////// status not found case /////////////////////////////////////////////////////////////////////
//
//    }

    @Test
    void listAllQuestions_Valid() throws Exception {
        Question testValidQuestion1 = new Question();
        testValidQuestion1.setQuestionTasks(null);
        testValidQuestion1.setContent("testValidQuestion1");

        Question testValidQuestion2 = new Question();
        testValidQuestion2.setQuestionTasks(null);
        testValidQuestion2.setContent("testValidQuestion2");

        List<Question> foundQuestions2 = new ArrayList<>();

        foundQuestions2.add(testValidQuestion1);
        foundQuestions2.add(testValidQuestion2);

        when(questionRepository.findAll()).thenReturn(foundQuestions2);

        mockMvc.perform(get("/question/all")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        ///////////////////////////////////////////////////////////////////// case in which we successfully retrieve all questions /////////////////////////////////////////////////////////////////////
    }

    @Test
    void listQuestion_NotFound() throws Exception {

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case /////////////////////////////////////////////////////////////////////

    }

    @Test
    void listQuestion_Valid() throws Exception {
        Question testQuestion = new Question();
        testQuestion.setQuestionTasks(null);
        testQuestion.setContent("testQuestion");
        testQuestion.setId(0);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(testQuestion));

        mockMvc.perform(get("/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
        ///////////////////////////////////////////////////////////////////// case in which we successfully find the question /////////////////////////////////////////////////////////////////////
    }

    @Test
    void updateQuestion_BadRequest1() throws Exception {
        Question testInvalidQuestion1 = new Question();
        testInvalidQuestion1.setId(null);
        testInvalidQuestion1.setQuestionTasks(null);
        testInvalidQuestion1.setContent("testInvalidQuestion1");

        mockMvc.perform(put("/question/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidQuestion1)))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// case 1 of bad request - testInvalidQuestion1.id == null /////////////////////////////////////////////////////////////////////

    }

    @Test
    void updateQuestion_BadRequest2() throws Exception {
        Question testInvalidQuestion2 = new Question();
        testInvalidQuestion2.setId(1);
        testInvalidQuestion2.setQuestionTasks(null);
        testInvalidQuestion2.setContent("testInvalidQuestion2");

        mockMvc.perform(put("/question/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidQuestion2)))
                .andExpect(status().isBadRequest());
        ///////////////////////////////////////////////////////////////////// case 2 of bad request - testInvalidQuestion2.id != {id} /////////////////////////////////////////////////////////////////////

    }

    @Test
    void updateQuestion_NotFound() throws Exception {
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        Question testInvalidQuestion3 = new Question();
        testInvalidQuestion3.setId(0);
        testInvalidQuestion3.setQuestionTasks(null);
        testInvalidQuestion3.setContent("testInvalidQuestion3");
        mockMvc.perform(put("/question/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testInvalidQuestion3)))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case - couldn't find a question with the given id to update /////////////////////////////////////////////////////////////////////


    }

    @Test
    void updateQuestion_Valid() throws Exception {
        Question testValidQuestion = new Question();
        testValidQuestion.setId(0);
        testValidQuestion.setQuestionTasks(null);
        testValidQuestion.setContent("testValidQuestion");
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidQuestion));
        when(questionRepository.save(any(Question.class))).thenReturn(testValidQuestion);
        mockMvc.perform(put("/question/0")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(testValidQuestion)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(testValidQuestion.getContent()))
                .andExpect(jsonPath("$.id").value(testValidQuestion.getId()));
        ///////////////////////////////////////////////////////////////////// case in which we successfully update the question with the given id /////////////////////////////////////////////////////////////////////
    }

    @Test
    void deleteQuestion_NotFound() throws Exception {
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(delete("/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        ///////////////////////////////////////////////////////////////////// status not found case - couldn't find a question with the given id to delete /////////////////////////////////////////////////////////////////////

    }

    @Test
    void deleteQuestion_Valid() throws Exception {
        Question testValidQuestion = new Question();
        testValidQuestion.setId(0);
        testValidQuestion.setQuestionTasks(null);
        testValidQuestion.setContent("testValidQuestion");
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(testValidQuestion));
        mockMvc.perform(delete("/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        ///////////////////////////////////////////////////////////////////// case in which we successfully delete the question with the given id /////////////////////////////////////////////////////////////////////
    }
}
