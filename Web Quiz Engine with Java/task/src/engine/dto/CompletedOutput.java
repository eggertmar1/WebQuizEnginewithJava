package engine.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CompletedOutput {
    private Integer quizId;
    private Date completedAt;

    public CompletedOutput() {
    }

    public CompletedOutput(Integer quizId, Date completedAt, Long userId) {
        this.quizId = quizId;
        this.completedAt = completedAt;
    }

    public Integer getQuizId() {
        return quizId;
    }

    public void setQuizId(Integer quizId) {
        this.quizId = quizId;
    }

    public String getCompletedAtIso() {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return isoFormat.format(completedAt);
    }
}
