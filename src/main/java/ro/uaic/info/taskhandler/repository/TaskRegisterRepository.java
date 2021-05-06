package ro.uaic.info.taskhandler.repository;

import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Student;
import ro.uaic.info.taskhandler.entity.TaskRegistration;
import ro.uaic.info.taskhandler.entity.TaskRegistrationPK;

import java.util.Optional;

public interface TaskRegisterRepository extends CrudRepository<TaskRegistration, TaskRegistrationPK>
{
    public Optional<TaskRegistration> findByTaskIdAndStudentId(Integer task_id, Integer student_id);
}
