package ro.uaic.info.taskhandler.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class AnswerPK implements Serializable
{
    private Integer studentId;
    private Integer taskId;
    private Integer questionId;

    public AnswerPK()
    {
    }

    public AnswerPK(Integer studentId, Integer taskId, Integer questionId)
    {
        this.studentId = studentId;
        this.taskId = taskId;
        this.questionId = questionId;
    }

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
