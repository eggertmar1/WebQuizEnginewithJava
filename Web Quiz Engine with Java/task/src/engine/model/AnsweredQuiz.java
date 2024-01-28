package engine.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
public class AnsweredQuiz {
    /*
        For some reason, the project does not want to save if the quiz was a success, so this is only the quiz entry and nothing more.
    */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = true)
    private Long answerId; // this is the id of the AnswerInput object - not the quiz id
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    private Integer id;
    private String completedAt;

    public AnsweredQuiz() {
    }

    public Long getAnswerId() {
        return answerId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}