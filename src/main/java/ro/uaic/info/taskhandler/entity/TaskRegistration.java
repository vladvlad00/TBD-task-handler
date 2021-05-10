package ro.uaic.info.taskhandler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class TaskRegistration {
    @JsonIgnore
    @EmbeddedId
    private TaskRegistrationPK id;


    @ManyToOne
    @JoinColumn(name="studentId")
    @MapsId("studentId")
    Student student;

    @ManyToOne
    @JoinColumn(name="taskId")
    @MapsId("taskId")
    Task task;

    String status;

    public TaskRegistrationPK getId() {
        return id;
    }

    public void setId(TaskRegistrationPK id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

