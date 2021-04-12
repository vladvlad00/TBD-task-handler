package ro.uaic.info.taskhandler.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Professor
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Integer id;

    private String name;

    @ManyToMany(mappedBy = "taskProfessors")
    Set<Task> professorTasks;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
