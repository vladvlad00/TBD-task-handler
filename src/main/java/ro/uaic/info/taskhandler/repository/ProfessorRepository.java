package ro.uaic.info.taskhandler.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Professor;

public interface ProfessorRepository extends CrudRepository<Professor, Integer>
{
}
