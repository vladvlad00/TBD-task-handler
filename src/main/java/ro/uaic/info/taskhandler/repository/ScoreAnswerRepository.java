package ro.uaic.info.taskhandler.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ro.uaic.info.taskhandler.entity.Answer;
import ro.uaic.info.taskhandler.entity.ScoreAnswer;
import ro.uaic.info.taskhandler.entity.ScoreAnswerPK;

public interface ScoreAnswerRepository extends CrudRepository<ScoreAnswer, ScoreAnswerPK> {
    @Query(value = "select * from score_answer where task_id=?1 and student_id=?2", nativeQuery = true)
    Iterable<ScoreAnswer> findByTaskStudentId(Integer taskId, Integer studentId);

    @Query(value = "select * from score_answer where task_id=?1 and question_id=?2", nativeQuery = true)
    Iterable<ScoreAnswer> findByTaskQuestionId(Integer taskId, Integer questionId);
}
