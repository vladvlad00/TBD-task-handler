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
import ro.uaic.info.taskhandler.entity.Task;
import ro.uaic.info.taskhandler.repository.QuestionRepository;

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
@WebMvcTest(TaskQuestionsController.class)
class TaskQuestionsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private QuestionRepository questionRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Test
    void createTaskQuestion_BadRequest1() throws Exception {           //taskId == null
        Map<String, Integer> invalidTaskQuestion1 = new HashMap<>();
        invalidTaskQuestion1.put("questionId",0);
        invalidTaskQuestion1.put("taskId",null);

        mockMvc.perform(post("/task_question/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskQuestion1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskQuestion_BadRequest2() throws Exception{            //questionId == null
        Map<String, Integer> invalidTaskQuestion2= new HashMap<>();
        invalidTaskQuestion2.put("questionId",null);
        invalidTaskQuestion2.put("taskId",0);

        mockMvc.perform(post("/task_question/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskQuestion2)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskQuestion_BadRequest3() throws Exception {           //taskOpt.isEmpty() == True
        Map<String, Integer> invalidTaskQuestion1 = new HashMap<>();
        Integer questionId = 0;
        Integer taskId = 0;
        invalidTaskQuestion1.put("questionId",questionId);
        invalidTaskQuestion1.put("taskId",taskId);

        Question question = new Question();
        question.setQuestionTasks(null);
        question.setContent("question");
        question.setId(questionId);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));

        mockMvc.perform(post("/task_question/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskQuestion1)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTaskQuestion_BadRequest4() throws Exception {               //questionOpt.isEmpty() == True
        Map<String, Integer> invalidTaskQuestion1 = new HashMap<>();
        Integer questionId = 0;
        Integer taskId = 0;
        invalidTaskQuestion1.put("questionId",questionId);
        invalidTaskQuestion1.put("taskId",taskId);

        Task task = new Task();
        task.setTaskQuestions(null);
        task.setName("task");
        task.setId(questionId);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(post("/task_question/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(invalidTaskQuestion1)))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void createTaskQuestion_Valid() throws Exception {
//        Map<String, Integer> invalidTaskQuestion1 = new HashMap<>();
//        Integer questionId = 0;
//        Integer taskId = 0;
//        invalidTaskQuestion1.put("questionId",questionId);
//        invalidTaskQuestion1.put("taskId",taskId);
//
//        Task task = new Task();
//        task.setName("task");
//        task.setId(taskId);
//
//        Question question = new Question();
//        question.setContent("question");
//        question.setId(questionId);
//
//        Set<Question> taskSet = new HashSet<>();
//        taskSet.add(question);
//        task.setTaskQuestions(taskSet);
//
//        Set<Task> questionSet = new HashSet<>();
//        questionSet.add(task);
//        question.setQuestionTasks(questionSet);
//
//
//        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
//        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));
//        when(taskRepository.save(any(Task.class))).thenReturn(task);
//        when(questionRepository.save(any(Question.class))).thenReturn(question);
//
//        mockMvc.perform(post("/task_question/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(invalidTaskQuestion1)))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$").exists());
//    }


    @Test
    void listByQuestionId_NotFound() throws Exception {                                        //foundQuestion.isEmpty() == True
        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/task_question/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByQuestionId_Valid() throws Exception {
        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Set<Task> questionSet = new HashSet<>();
        questionSet.add(task);
        question.setQuestionTasks(questionSet);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));
        mockMvc.perform(get("/task_question/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

    @Test
    void listByTaskId_NotFound() throws Exception {                                                     //foundTask.isEmpty() == True
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        mockMvc.perform(get("/task_question/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void listByTaskId_Valid() throws Exception {
        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        Set<Question> taskSet = new HashSet<>();
        taskSet.add(question);
        task.setTaskQuestions(taskSet);

        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));
        mockMvc.perform(get("/task_question/task/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }

//    @Test
//    void DeleteTaskQuestion_BadRequest1() throws Exception {                           //questionId == null
//
//        Map<String,Integer> map = new HashMap<>();
//        map.put("questionId",null);
//        map.put("taskId",0);
//
//        mockMvc.perform(delete("/task_question/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(map)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void DeleteTaskQuestion_BadRequest2() throws Exception{                             //taskId == null
//
//        Map<String,Integer> map = new HashMap<>();
//        map.put("questionId",0);
//        map.put("taskId",null);
//
//        mockMvc.perform(delete("/task_question/")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(mapper.writeValueAsString(map)))
//                .andExpect(status().isBadRequest());
//    }


    @Test
    void DeleteTaskQuestion_BadRequest3() throws Exception{                                //empty question

        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.empty());
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_question/task/0/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskQuestion_BadRequest4() throws Exception{                                 //empty task


        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        mockMvc.perform(delete("/task_question/task/0/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void DeleteTaskQuestion_BadRequest5() throws Exception{                //task.getTaskQuestions().contains(question) == False


        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        Set<Question> taskSet = new HashSet<>();
        task.setTaskQuestions(taskSet);

        Set<Task> questionSet = new HashSet<>();
        questionSet.add(task);
        question.setQuestionTasks(questionSet);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_question/task/0/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void DeleteTaskQuestion_BadRequest6() throws Exception{                //question.getQuestionTasks().contains(task) == False


        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        Set<Question> taskSet = new HashSet<>();
        taskSet.add(question);
        task.setTaskQuestions(taskSet);

        Set<Task> questionSet = new HashSet<>();
        question.setQuestionTasks(questionSet);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_question/task/0/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void DeleteTaskQuestion_Valid() throws Exception{


        Task task = new Task();
        task.setName("task");
        task.setId(0);

        Question question = new Question();
        question.setContent("question");
        question.setId(0);

        Set<Question> taskSet = new HashSet<>();
        taskSet.add(question);
        task.setTaskQuestions(taskSet);

        Set<Task> questionSet = new HashSet<>();
        questionSet.add(task);
        question.setQuestionTasks(questionSet);

        when(questionRepository.findById(any(Integer.class))).thenReturn(Optional.of(question));
        when(taskRepository.findById(any(Integer.class))).thenReturn(Optional.of(task));

        mockMvc.perform(delete("/task_question/task/0/question/0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}