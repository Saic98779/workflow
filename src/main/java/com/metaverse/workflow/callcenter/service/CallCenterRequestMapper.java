package com.metaverse.workflow.callcenter.service;

import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.model.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CallCenterRequestMapper {
    public static Question mapQuestion(QuestionRequest request)
    {
        return Question.builder().questionName(request.getQuestion())
                .questionType(request.getQuestionFieldType())
                .answers(request.getOptions()== null ? "" :
                String.join(",", request.getOptions()))
                .build();
    }
    public static SubActivityQuestions mapSubActivityQuestions(SubActivityQuestionsRequest request)
    {
       return SubActivityQuestions.builder().subActivityId(request.getSubActivityId())
                .questionsIds(request.getQuestions()).build();

    }
    public static QuestionAnswers mapQuestionAnswer(Question question, String answer)
    {
        return QuestionAnswers.builder().question(question).answers(answer).build();
    }
    public static CallCenterVerification mapParticipantVerification(CallCenterVerificationRequest request, List<QuestionAnswers> questionAnswersList, User user,CallCenterVerificationStatus ccVerificationStatus)
    {
       return CallCenterVerification.builder()
               .verifiedBy(user)
               .verificationDate(DateUtil.stringToDate(request.getVerificationDate(),"dd-MM-yyyy"))
               .questionAnswers(questionAnswersList)
               .participantId(request.getParticipantId())
               .programId(request.getProgramId())
               .ccVerificationStatus(ccVerificationStatus)
               .build();
    }
}
