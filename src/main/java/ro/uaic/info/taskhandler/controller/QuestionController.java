package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Question;
import ro.uaic.info.taskhandler.repository.QuestionRepository;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/question")
public class QuestionController
{
    @Autowired
    private QuestionRepository questionRepository;

    @PostMapping("/")
    public ResponseEntity<Question> createQuestion(@RequestBody Question question)
    {
        if (question.getId() != null && questionRepository.findById(question.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Question createdQuestion = questionRepository.save(question);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdQuestion.getId()).toUri();
        return ResponseEntity.created(uri).body(createdQuestion);
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<ResponseEntity<Question>>> createQuestion(@RequestBody Iterable<Question> questions)
    {
        for (var question : questions)
        {
            if (question.getId() != null && questionRepository.findById(question.getId()).isPresent())
                return ResponseEntity.badRequest().build();
        }

        List<ResponseEntity<Question>> responses = new ArrayList<>();
        for (var question : questions)
        {
            Question createdQuestion = questionRepository.save(question);

            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}").buildAndExpand(createdQuestion.getId()).toUri();
            responses.add(ResponseEntity.created(uri).body(createdQuestion));
        }
        return ResponseEntity.ok().body(responses);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Question>> listAllQuestions()
    {
        Iterable<Question> foundQuestions = questionRepository.findAll();
        return ResponseEntity.ok(foundQuestions);
    }

    @GetMapping("/all_no_answer")
    public ResponseEntity<Iterable<Question>> listAllQuestionsNoAnswer()
    {
        Iterable<Question> foundQuestions = questionRepository.findAll();
        for(var question:foundQuestions){
            question.setCorrectAnswer(null);
        }
        return ResponseEntity.ok(foundQuestions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> listQuestion(@PathVariable Integer id)
    {
        Optional<Question> foundQuestion = questionRepository.findById(id);
        if (foundQuestion.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundQuestion.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question, @PathVariable Integer id)
    {
        if (question.getId() == null || !question.getId().equals(id))
            return ResponseEntity.badRequest().build();
        if (questionRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Question updatedQuestion = questionRepository.save(question);

        return ResponseEntity.ok(updatedQuestion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Question> deleteQuestion(@PathVariable Integer id)
    {
        if (questionRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        questionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
