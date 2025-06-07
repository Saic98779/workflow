package com.metaverse.workflow.callcenter.service;

import lombok.*;

import java.util.List;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CallCenterVerificationRequest {

    private Long programId;
    private Long participantId;
    private String verifiedBy;
    private String verificationDate;
    private List<QuestionAnswer> questionAnswerList;
    private Long verificationStatusId;

    @Builder
    @AllArgsConstructor
    @Getter
    @Setter
    public static class QuestionAnswer {
        private Integer questionId;
        private String[] answers;
    }

}
