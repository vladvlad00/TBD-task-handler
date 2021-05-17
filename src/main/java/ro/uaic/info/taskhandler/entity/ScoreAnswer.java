package ro.uaic.info.taskhandler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Map;

@Entity
public class ScoreAnswer
{
    @JsonIgnore
    @EmbeddedId
    private ScoreAnswerPK id;

    @ManyToOne
    @JoinColumn(name = "taskId")
    @MapsId("taskId")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "questionId")
    @MapsId("questionId")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "studentId")
    @MapsId("studentId")
    private Student student;

    private Integer scoreValue;

    public ScoreAnswer()
    {
    }

    public ScoreAnswer(ScoreAnswerPK id, Task task, Question question, Student student, Integer scoreValue)
    {
        this.id = id;
        this.task = task;
        this.question = question;
        this.student = student;
        this.scoreValue = scoreValue;
    }

    public ScoreAnswerPK getId()
    {
        return id;
    }

    public void setId(ScoreAnswerPK id)
    {
        this.id = id;
    }

    public Task getTask()
    {
        return task;
    }

    public void setTask(Task task)
    {
        this.task = task;
    }

    public Question getQuestion()
    {
        return question;
    }

    public void setQuestion(Question question)
    {
        this.question = question;
    }

    public Student getStudent()
    {
        return student;
    }

    public void setStudent(Student student)
    {
        this.student = student;
    }

    public Integer getScoreValue()
    {
        return scoreValue;
    }

    public void setScoreValue(Integer scoreValue)
    {
        this.scoreValue = scoreValue;
    }
}
