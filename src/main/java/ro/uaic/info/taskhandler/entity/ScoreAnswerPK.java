package ro.uaic.info.taskhandler.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ScoreAnswerPK implements Serializable
{
    private Integer studentId;
    private Integer taskId;
    private Integer questionId;

    public Integer getStudentId()
    {
        return studentId;
    }

    public void setStudentId(Integer studentId)
    {
        this.studentId = studentId;
    }

    public Integer getTaskId()
    {
        return taskId;
    }

    public void setTaskId(Integer taskId)
    {
        this.taskId = taskId;
    }

    public Integer getQuestionId()
    {
        return questionId;
    }

    public void setQuestionId(Integer questionId)
    {
        this.questionId = questionId;
    }
}