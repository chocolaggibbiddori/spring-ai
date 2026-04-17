package chocola.springai.dto.ch04;

import lombok.Data;

@Data
public class ReviewClassification {

    private String review;
    private Sentiment classification;

    public enum Sentiment {

        POSITIVE, NEUTRAL, NEGATIVE
    }
}
