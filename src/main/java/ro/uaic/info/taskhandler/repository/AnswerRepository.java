package ro.uaic.info.taskhandler.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Answer;
import ro.uaic.info.taskhandler.entity.AnswerPK;

public interface AnswerRepository extends CrudRepository<Answer, AnswerPK>
{
    @Query(value = "select * from answer where task_id=?1 and student_id=?2", nativeQuery = true)
    Iterable<Answer> findByTaskStudentId(Integer taskId, Integer studentId);

    @Query(value = "select * from answer where task_id=?1 and question_id=?2", nativeQuery = true)
    Iterable<Answer> findByTaskQuestionId(Integer taskId, Integer questionId);
}
