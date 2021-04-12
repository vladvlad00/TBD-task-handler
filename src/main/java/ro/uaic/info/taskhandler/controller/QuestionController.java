package ro.uaic.info.taskhandler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ro.uaic.info.taskhandler.entity.Question;
import ro.uaic.info.taskhandler.repository.QuestionRepository;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping(path="/question")
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

    @GetMapping("/all")
    public ResponseEntity<Iterable<Question>> listAllQuestions()
    {
        Iterable<Question> foundUsers = questionRepository.findAll();
        if (foundUsers == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundUsers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Question> listQuestion(@PathVariable Integer id)
    {
        Optional<Question> foundUser = questionRepository.findById(id);
        if (foundUser.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundUser.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Question> updateQuestion(@RequestBody Question question, @PathVariable Integer id)
    {
        if (question.getId() == null || !question.getId().equals(id))
            return ResponseEntity.badRequest().build();
        if (questionRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Question updatedQuestion = questionRepository.save(question);

        if (updatedQuestion == null)
            return ResponseEntity.notFound().build();

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
