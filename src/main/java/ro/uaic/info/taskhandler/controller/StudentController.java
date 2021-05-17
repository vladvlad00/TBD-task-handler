package ro.uaic.info.taskhandler.controller;

import ro.uaic.info.taskhandler.entity.*;
import ro.uaic.info.taskhandler.repository.AnswerRepository;
import ro.uaic.info.taskhandler.repository.ScoreAnswerRepository;
import ro.uaic.info.taskhandler.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/student")
public class StudentController
{
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private ScoreAnswerRepository scoreAnswerRepository;

    @PostMapping("/")
    public ResponseEntity<Student> createStudent(@RequestBody Student student)
    {
        if (student.getId() != null && studentRepository.findById(student.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Student createdStudent = studentRepository.save(student);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdStudent.getId()).toUri();
        return ResponseEntity.created(uri).body(createdStudent);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Student>> listAllStudents()
    {
        Iterable<Student> foundStudents =studentRepository.findAll();
        return ResponseEntity.ok(foundStudents);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> listStudent(@PathVariable Integer id)
    {
        Optional<Student> foundStudent = studentRepository.findById(id);
        if (foundStudent.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundStudent.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@RequestBody Student student, @PathVariable Integer id)
    {
        if (student.getId() == null || !student.getId().equals(id))
            return ResponseEntity.badRequest().build();

        if (studentRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Student updatedStudent = studentRepository.save(student);

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Integer id)
    {
        Optional<Student> studentOpt = studentRepository.findById(id);
        if (studentOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Student student = studentOpt.get();

        if(student.getStudentTasks()!=null) {
            for (TaskRegistration taskRegistration : student.getStudentTasks()) {
                taskRegistration.getTask().getTaskStudents().remove(taskRegistration);
            }
            //student.getStudentTasks().removeAll(student.getStudentTasks());
        }


        if(student.getAnswers()!=null){
            for(Answer answer : student.getAnswers())
                answerRepository.deleteById(answer.getId());
        }

        if(student.getScoreAnswers()!=null){
            for(ScoreAnswer scoreAnswer : student.getScoreAnswers())
                scoreAnswerRepository.deleteById(scoreAnswer.getId());
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
