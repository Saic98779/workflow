package com.metaverse.workflow.program.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.metaverse.workflow.model.Question;
import com.metaverse.workflow.model.QuestionAnswers;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipantVerificationResponse {

    private Long participantId;
    private String participantName;
    private Character gender;
    private String category;
    private Character disability;
    private Long aadharNo;
    private Long mobileNo;
    private String email;
    private String designation;
    private String organizationName;
    private String verifiedBy;
    private Long ccVerificationStatusId;
    private String ccVerificationStatus;
    private String memberId;
    private List<QuestionAnswers> questionAnswersList;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class QuestionAnswers {

        private Question question;
        private String answers ;

    }
}

