package ro.uaic.info.taskhandler.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class TaskRegistrationPK implements Serializable{
    private Integer studentId;
    private Integer taskId;

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }
}
