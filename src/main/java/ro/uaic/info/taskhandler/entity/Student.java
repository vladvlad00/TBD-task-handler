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
    @ManyToMany(mappedBy = "taskStudents")
    Set<Task> studentTasks;

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

    public Set<Task> getStudentTasks(){return studentTasks;}

    public void setStudentTasks(Set<Task> studentTasks){this.studentTasks=studentTasks;}
}
