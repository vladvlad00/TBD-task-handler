package ro.uaic.info.taskhandler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Map;

@Entity
public class Answer
{
    @JsonIgnore
    @EmbeddedId
    private AnswerPK id;

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

    private String content;

    public AnswerPK getId()
    {
        return id;
    }

    public void setId(AnswerPK id)
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

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }
}
