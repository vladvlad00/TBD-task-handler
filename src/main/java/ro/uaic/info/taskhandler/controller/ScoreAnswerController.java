package ro.uaic.info.taskhandler.controller;

import com.netflix.servo.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.*;
import ro.uaic.info.taskhandler.repository.ScoreAnswerRepository;
import ro.uaic.info.taskhandler.repository.QuestionRepository;
import ro.uaic.info.taskhandler.repository.StudentRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path="/score_answer")
public class ScoreAnswerController
{
    @Autowired
    private ScoreAnswerRepository scoreAnswerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/")
    public ResponseEntity<ScoreAnswer> createScoreAnswer(@RequestBody Map<String,String> scoreAnswer)
    {
        Integer studentId;
        Integer questionId;
        Integer taskId;
        Integer scoreValue;
        try
        {
            studentId = Integer.parseInt(scoreAnswer.get("studentId"));
            questionId = Integer.parseInt(scoreAnswer.get("questionId"));
            taskId = Integer.parseInt(scoreAnswer.get("taskId"));
            scoreValue = Integer.parseInt(scoreAnswer.get("scoreValue"));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

        if (studentId == null || questionId == null || taskId == null || scoreValue == null)
            return ResponseEntity.badRequest().build();

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (studentOpt.isEmpty() || questionOpt.isEmpty() || taskOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Student student = studentOpt.get();
        Question question = questionOpt.get();
        Task task = taskOpt.get();

        ScoreAnswerPK id = new ScoreAnswerPK();
        id.setQuestionId(questionId);
        id.setStudentId(studentId);
        id.setTaskId(taskId);

        if (scoreAnswerRepository.findById(id).isPresent())
            return ResponseEntity.badRequest().build();

        ScoreAnswer scoreAnswerObj = new ScoreAnswer();
        scoreAnswerObj.setId(id);
        scoreAnswerObj.setStudent(student);
        scoreAnswerObj.setQuestion(question);
        scoreAnswerObj.setTask(task);
        scoreAnswerObj.setScoreValue(scoreValue);

        student.getScoreAnswers().add(scoreAnswerObj);
        question.getScoreAnswers().add(scoreAnswerObj);
        task.getScoreAnswers().add(scoreAnswerObj);

        ScoreAnswer createdScoreAnswer = scoreAnswerRepository.save(scoreAnswerObj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdScoreAnswer.getId()).toUri();
        return ResponseEntity.created(uri).body(createdScoreAnswer);
    }

    @GetMapping("/task_student/{taskId}/{studentId}")
    public ResponseEntity<Iterable<ScoreAnswer>> listScoreAnswerByTaskStudentId(@PathVariable Integer taskId, @PathVariable Integer studentId)
    {
        Iterable<ScoreAnswer> foundScoreAnswers = scoreAnswerRepository.findByTaskStudentId(taskId, studentId);
        int count = 0;
        for (var scoreAnswer : foundScoreAnswers)
            count++;
        if (count == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundScoreAnswers);
    }

    @GetMapping("/task_question/{taskId}/{questionId}")
    public ResponseEntity<Iterable<ScoreAnswer>> listScoreAnswerByTaskQuestionId(@PathVariable Integer taskId, @PathVariable Integer questionId)
    {
        Iterable<ScoreAnswer> foundScoreAnswers = scoreAnswerRepository.findByTaskQuestionId(taskId, questionId);
        int count = 0;
        for (var scoreAnswer : foundScoreAnswers)
            count++;
        if (count == 0)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundScoreAnswers);
    }

    @GetMapping("/task_question_student/{taskId}/{questionId}/{studentId}")
    public ResponseEntity<ScoreAnswer> listScoreAnswerByTaskQuestionStudentId(@PathVariable Integer taskId,
                                                                    @PathVariable Integer questionId,
                                                                    @PathVariable Integer studentId)
    {
        var id = new ScoreAnswerPK();
        id.setQuestionId(questionId);
        id.setStudentId(studentId);
        id.setTaskId(taskId);
        Optional<ScoreAnswer> foundScoreAnswer = scoreAnswerRepository.findById(id);
        if (foundScoreAnswer.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundScoreAnswer.get());
    }

    @PutMapping("/{studentIdPath}/{taskIdPath}/{questionIdPath}")
    public ResponseEntity<ScoreAnswer> updateScoreAnswer(@RequestBody Map<String,String> scoreAnswer, @PathVariable Integer studentIdPath,
                                               @PathVariable Integer taskIdPath, @PathVariable Integer questionIdPath)
    {
        Integer studentId;
        Integer questionId;
        Integer taskId;
        Integer scoreValue;
        try
        {
            studentId = Integer.parseInt(scoreAnswer.get("studentId"));
            questionId = Integer.parseInt(scoreAnswer.get("questionId"));
            taskId = Integer.parseInt(scoreAnswer.get("taskId"));
            scoreValue = Integer.parseInt(scoreAnswer.get("scoreValue"));
        }
        catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

        if (studentId == null || questionId == null || taskId == null || scoreValue == null ||
                !studentId.equals(studentIdPath) || !questionId.equals(questionIdPath) || !taskId.equals(taskIdPath))
            return ResponseEntity.badRequest().build();

        ScoreAnswerPK id = new ScoreAnswerPK();
        id.setTaskId(taskId);
        id.setStudentId(studentId);
        id.setQuestionId(questionId);
        Optional<ScoreAnswer> scoreAnswerOpt = scoreAnswerRepository.findById(id);
        if (scoreAnswerOpt.isEmpty())
            return ResponseEntity.notFound().build();

        ScoreAnswer scoreAnswerObj = scoreAnswerOpt.get();
        scoreAnswerObj.setScoreValue(scoreValue);

        ScoreAnswer updatedScoreAnswer = scoreAnswerRepository.save(scoreAnswerObj);

        if (updatedScoreAnswer == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updatedScoreAnswer);
    }
}
