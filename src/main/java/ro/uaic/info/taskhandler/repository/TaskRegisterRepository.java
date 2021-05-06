package ro.uaic.info.taskhandler.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Student;
import ro.uaic.info.taskhandler.entity.TaskRegistration;

import java.util.Optional;

public interface TaskRegisterRepository extends CrudRepository<TaskRegistration, Integer>
{
    public Optional<TaskRegistration> findByTaskIdAndStudentId(Integer task_id, Integer student_id);
}
