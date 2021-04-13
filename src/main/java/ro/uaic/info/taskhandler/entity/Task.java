package ro.uaic.info.taskhandler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "professorTasks",
            joinColumns = @JoinColumn(name = "professorId"),
            inverseJoinColumns = @JoinColumn(name = "taskId")
    )
    Set<Professor> taskProfessors;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "taskQuestions",
            joinColumns = @JoinColumn(name = "questionId"),
            inverseJoinColumns = @JoinColumn(name = "taskId")
    )
    Set<Professor> taskQuestions;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime()
    {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime)
    {
        this.endTime = endTime;
    }

    public Set<Professor> getTaskProfessors()
    {
        return taskProfessors;
    }

    public void setTaskProfessors(Set<Professor> taskProfessors)
    {
        this.taskProfessors = taskProfessors;
    }

    public Set<Professor> getTaskQuestions()
    {
        return taskQuestions;
    }

    public void setTaskQuestions(Set<Professor> taskQuestions)
    {
        this.taskQuestions = taskQuestions;
    }
}
