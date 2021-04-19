package ro.uaic.info.taskhandler.controller;

import ro.uaic.info.taskhandler.entity.Professor;
import ro.uaic.info.taskhandler.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/professor")
public class ProfessorController
{
    @Autowired
    private ProfessorRepository professorRepository;

    @PostMapping("/")
    public ResponseEntity<Professor> createProfessor(@RequestBody Professor professor)
    {
        if (professor.getId() != null && professorRepository.findById(professor.getId()).isPresent())
            return ResponseEntity.badRequest().build();

        Professor createdProfessor = professorRepository.save(professor);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(createdProfessor.getId()).toUri();
        return ResponseEntity.created(uri).body(createdProfessor);
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<Professor>> listAllProfessors()
    {
        Iterable<Professor> foundProfessors =professorRepository.findAll();
        if (foundProfessors == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundProfessors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> listProfessor(@PathVariable Integer id)
    {
        Optional<Professor> foundProfessor = professorRepository.findById(id);
        if (foundProfessor.isEmpty())
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(foundProfessor.get());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> updateProfessor(@RequestBody Professor professor, @PathVariable Integer id)
    {
        if (professor.getId() == null || !professor.getId().equals(id))
            return ResponseEntity.badRequest().build();

        if (professorRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        Professor updatedProfessor = professorRepository.save(professor);

        if (updatedProfessor == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(updatedProfessor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Professor> deleteProfessor(@PathVariable Integer id)
    {
        if (professorRepository.findById(id).isEmpty())
            return ResponseEntity.notFound().build();

        professorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
