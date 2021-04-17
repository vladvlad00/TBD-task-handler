package ro.uaic.info.taskhandler.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer>
{
}
