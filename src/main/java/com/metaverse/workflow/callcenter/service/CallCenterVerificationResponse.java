package com.metaverse.workflow.callcenter.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.model.Question;
import com.metaverse.workflow.model.QuestionAnswers;
import jakarta.persistence.Column;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallCenterVerificationResponse {

    private Long programId;
    private Long participantId;
    private String verifiedBy;
    private String verificationDate;
    private List<QuestionAnswers> questionAnswers;
    private String verificationStatus;


   /* @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuestionAnswers {

        private Question question;
        private String answers ;

    }
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Question{

        private String questionName;
        private String[] answers;
    }
*/


}

