package ro.uaic.info.taskhandler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
public class Task
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer id;

    private String name;

    private String subject;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "professorTasks",
            joinColumns = @JoinColumn(name = "taskId"),
            inverseJoinColumns = @JoinColumn(name = "professorId")
    )
    Set<Professor> taskProfessors;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "taskQuestions",
            joinColumns = @JoinColumn(name = "taskId"),
            inverseJoinColumns = @JoinColumn(name = "questionId")
    )
    Set<Question> taskQuestions;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    Set<TaskRegistration> taskStudents;

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<Answer> answers)
    {
        this.answers = answers;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private List<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "task")
    private List<ScoreAnswer> scoreAnswers;

    public List<ScoreAnswer> getScoreAnswers() {
        return scoreAnswers;
    }

    public void setScoreAnswers(List<ScoreAnswer> scoreAnswers) {
        this.scoreAnswers = scoreAnswers;
    }

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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public Set<Question> getTaskQuestions()
    {
        return taskQuestions;
    }

    public void setTaskQuestions(Set<Question> taskQuestions)
    {
        this.taskQuestions = taskQuestions;
    }

    public Set<TaskRegistration> getTaskStudents(){ return taskStudents;}

    public void setTaskStudents(Set<TaskRegistration> taskStudents){this.taskStudents=taskStudents;}
}
