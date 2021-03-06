package ro.uaic.info.taskhandler.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Entity
public class Question
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    private String content;

    private String correctAnswer;

    private Integer maxPoints;

    private String questionType;

    @JsonIgnore
    @ManyToMany(mappedBy = "taskQuestions")
    Set<Task> questionTasks;

    public List<Answer> getAnswers()
    {
        return answers;
    }

    public void setAnswers(List<Answer> answers)
    {
        this.answers = answers;
    }

    @JsonIgnore
    @OneToMany(mappedBy = "question")
    private List<Answer> answers;

    @JsonIgnore
    @OneToMany(mappedBy = "question")
    private List<ScoreAnswer> scoreAnswers;

    public List<ScoreAnswer> getScoreAnswers()
    {
        return scoreAnswers;
    }

    public void setScoreAnswers(List<ScoreAnswer> scoreAnswers)
    {
        this.scoreAnswers = scoreAnswers;
    }

    public String getQuestionType()
    {
        return questionType;
    }

    public void setQuestionType(String questionType)
    {
        this.questionType = questionType;
    }

    public void setMaxPoints(Integer maxPoints)
    {
        this.maxPoints = maxPoints;
    }

    public Integer getMaxPoints()
    {
        return maxPoints;
    }

    public Integer getId()
    {
        return Id;
    }

    public void setId(Integer id)
    {
        Id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getCorrectAnswer()
    {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer)
    {
        this.correctAnswer = correctAnswer;
    }

    public Set<Task> getQuestionTasks()
    {
        return questionTasks;
    }

    public void setQuestionTasks(Set<Task> questionTasks)
    {
        this.questionTasks = questionTasks;
    }
}
