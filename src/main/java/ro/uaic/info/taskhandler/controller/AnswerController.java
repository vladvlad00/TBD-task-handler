package ro.uaic.info.taskhandler.controller;

import com.netflix.servo.util.Iterables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.*;
import ro.uaic.info.taskhandler.repository.AnswerRepository;
import ro.uaic.info.taskhandler.repository.QuestionRepository;
import ro.uaic.info.taskhandler.repository.StudentRepository;
import ro.uaic.info.taskhandler.repository.TaskRepository;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/answer")
public class AnswerController
{
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/")
    public ResponseEntity<Answer> createAnswer(@RequestBody Map<String, String> answer)
    {
        Integer studentId;
        Integer questionId;
        Integer taskId;
        String content;
        try
        {
            studentId = Integer.parseInt(answer.get("studentId"));
            questionId = Integer.parseInt(answer.get("questionId"));
            taskId = Integer.parseInt(answer.get("taskId"));
            content = answer.get("content");
        } catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

        if (content == null)
            return ResponseEntity.badRequest().build();

        Optional<Student> studentOpt = studentRepository.findById(studentId);
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        Optional<Task> taskOpt = taskRepository.findById(taskId);

        if (studentOpt.isEmpty() || questionOpt.isEmpty() || taskOpt.isEmpty())
            return ResponseEntity.badRequest().build();

        Student student = studentOpt.get();
        Question question = questionOpt.get();
        Task task = taskOpt.get();

        AnswerPK id = new AnswerPK(studentId, taskId, questionId);

        if (answerRepository.findById(id).isPresent())
            return ResponseEntity.badRequest().build();

        Answer answerObj = new Answer(id, task, question, student, content);

        student.getAnswers().add(answerObj);
        question.getAnswers().add(answerObj);
        task.getAnswers().add(answerObj);

        Answer createdAnswer = answerRepository.save(answerObj);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/task/{taskId}/question/{questionId}/student/{studentId}")
                .buildAndExpand(questionId, taskId, studentId).toUri();
        return ResponseEntity.created(uri).body(createdAnswer);
    }

    @GetMapping("/task/{taskId}/student/{studentId}")
    public ResponseEntity<Iterable<Answer>> listAnswerByTaskStudentId(@PathVariable Integer taskId, @PathVariable Integer studentId)
    {
        Iterable<Answer> foundAnswers = answerRepository.findByTaskStudentId(taskId, studentId);

        if (((Collection<?>) foundAnswers).isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundAnswers);
    }

    @GetMapping("/task/{taskId}/question/{questionId}")
    public ResponseEntity<Iterable<Answer>> listAnswerByTaskQuestionId(@PathVariable Integer taskId, @PathVariable Integer questionId)
    {
        Iterable<Answer> foundAnswers = answerRepository.findByTaskQuestionId(taskId, questionId);

        if (((Collection<?>) foundAnswers).isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundAnswers);
    }

    @GetMapping("/task/{taskId}/question/{questionId}/student/{studentId}")
    public ResponseEntity<Answer> listAnswerByTaskQuestionStudentId(@PathVariable Integer taskId, @PathVariable Integer questionId, @PathVariable Integer studentId)
    {
        var id = new AnswerPK(studentId, taskId, questionId);
        Optional<Answer> foundAnswer = answerRepository.findById(id);
        if (foundAnswer.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundAnswer.get());
    }

    @PutMapping("/task/{taskIdPath}/question/{questionIdPath}/student/{studentIdPath}")
    public ResponseEntity<Answer> updateAnswer(@RequestBody Map<String, String> answer, @PathVariable Integer studentIdPath,
                                               @PathVariable Integer taskIdPath, @PathVariable Integer questionIdPath)
    {
        Integer studentId;
        Integer questionId;
        Integer taskId;
        String content;
        try
        {
            studentId = Integer.parseInt(answer.get("studentId"));
            questionId = Integer.parseInt(answer.get("questionId"));
            taskId = Integer.parseInt(answer.get("taskId"));
            content = answer.get("content");
        } catch (Exception e)
        {
            return ResponseEntity.badRequest().build();
        }

        if (content == null || !studentId.equals(studentIdPath) ||
                !questionId.equals(questionIdPath) || !taskId.equals(taskIdPath))
            return ResponseEntity.badRequest().build();

        AnswerPK id = new AnswerPK(studentId, taskId, questionId);
        Optional<Answer> answerOpt = answerRepository.findById(id);
        if (answerOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Answer answerObj = answerOpt.get();
        answerObj.setContent(content);

        Answer updatedAnswer = answerRepository.save(answerObj);

        return ResponseEntity.ok(updatedAnswer);
    }
}
