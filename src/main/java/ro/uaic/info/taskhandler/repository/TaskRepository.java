package ro.uaic.info.taskhandler.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Task;

public interface TaskRepository extends CrudRepository<Task, Integer>
{
}
