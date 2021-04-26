package ro.uaic.info.taskhandler.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Student;

public interface StudentRepository extends CrudRepository<Student, Integer>
{
}
