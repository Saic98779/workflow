package com.metaverse.workflow.callcenter.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.CallCenterVerification;
import com.metaverse.workflow.model.CallCenterVerificationStatus;
import com.metaverse.workflow.model.Question;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class CallCenterResponseMapper {

    public static QuestionResponse mapQuestion(Question question)
    {
        return QuestionResponse.builder()
                .QuestionId(question.getQuestionId())
                .question(question.getQuestionName())
                .questionFieldType(question.getQuestionType())
                .options(question.getAnswers()==null ? Collections.emptyList()
                        : Arrays.stream(question.getAnswers().split(","))
                        .collect(Collectors.toList()))
                .build();
    }


    public static CallCenterVerificationResponse mapParticipantVerification(CallCenterVerification callCenterVerification, CallCenterVerificationStatus verificationStatus) {
        return CallCenterVerificationResponse.builder()
                .participantId(callCenterVerification.getParticipantId())
                .programId(callCenterVerification.getProgramId())
                .verifiedBy(callCenterVerification.getVerifiedBy().getUserId())
                .verificationDate(DateUtil.dateToString(callCenterVerification.getVerificationDate(),"dd-MM-yyyy"))
                .questionAnswers(callCenterVerification.getQuestionAnswers())
                .verificationStatus(verificationStatus.getVerificationDetails())
                .build();
    }
}
