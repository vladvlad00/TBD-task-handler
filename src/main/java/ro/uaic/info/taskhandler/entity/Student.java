package ro.uaic.info.taskhandler.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Integer id;

    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    Set<TaskRegistration> studentTasks;

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<Answer> answers)
    {
        this.answers = answers;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<ScoreAnswer> ScoreAnswers;

    public List<ScoreAnswer> getScoreAnswers() { return ScoreAnswers; }

    public void setScoreAnswers(List<ScoreAnswer> scoreAnswers) { ScoreAnswers = scoreAnswers; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<TaskRegistration> getStudentTasks(){return studentTasks;}

    public void setStudentTasks(Set<TaskRegistration> studentTasks){this.studentTasks=studentTasks;}
}
