package ro.uaic.info.taskhandler.controller;

import ro.uaic.info.taskhandler.entity.Student;
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
        if (foundStudents == null)
            return ResponseEntity.notFound().build();
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

        if (updatedStudent == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Integer id)
    {
        if (studentRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
