package ro.uaic.info.taskhandler.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Professor {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private  Integer Id;
    private String Name;

    public void setId(Integer id) {
        Id = id;
    }

    public void setName(String name) {
        Name = name;
    }

    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }
}
