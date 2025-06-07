package com.metaverse.workflow.callcenter.service;

import com.metaverse.workflow.activity.repository.SubActivityRepository;
import com.metaverse.workflow.callcenter.repository.CallCenterVerificationRepository;
import com.metaverse.workflow.callcenter.repository.CallCenterVerificationStatusRepository;
import com.metaverse.workflow.callcenter.repository.QuestionRepository;
import com.metaverse.workflow.callcenter.repository.SubActivityQuestionsRepository;
import com.metaverse.workflow.common.response.WorkflowResponse;
import com.metaverse.workflow.common.util.DateUtil;
import com.metaverse.workflow.exceptions.DataException;
import com.metaverse.workflow.login.repository.LoginRepository;
import com.metaverse.workflow.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CallCenterServiceAdapter implements CallCenterService {

    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    CallCenterVerificationStatusRepository ccVerificationStatusRepository;
    @Autowired
    SubActivityRepository subActivityRepository;
    @Autowired
    SubActivityQuestionsRepository subActivityQuestionsRepository;
    @Autowired
    CallCenterVerificationRepository ccVerificationRepository;
    @Autowired
    LoginRepository loginRepository;

    @Override
    public WorkflowResponse saveQuestion(QuestionRequest request) {
        Question question = CallCenterRequestMapper.mapQuestion(request);
        Question savedQuestion = questionRepository.save(question);
        return WorkflowResponse.builder().message("Question saved successfully").status(200).data(savedQuestion).build();
    }

    @Override
    public WorkflowResponse getAllQuestion() {
        List<Question> questionList = questionRepository.findAll();
        if (questionList.isEmpty())
            return WorkflowResponse.builder().message("Questions Not found").status(400).build();
        List<QuestionResponse> questionResponses = questionList.stream().map(questions -> CallCenterResponseMapper.mapQuestion(questions)).collect(Collectors.toList());
        return WorkflowResponse.builder().message("Success").status(200).data(questionResponses).build();
    }

    @Override
    public WorkflowResponse getQuestionById(Integer id) {

        Optional<Question> question = questionRepository.findById(id);
        if (!question.isPresent())
            return WorkflowResponse.builder().message(id + " is a wrong Question id ").status(400).build();
        return WorkflowResponse.builder().message("success").status(200).data(question.get()).build();
    }


    @Override
    public WorkflowResponse getAllVerificationStatus() {
        List<CallCenterVerificationStatus> statusList = ccVerificationStatusRepository.findAll();
        if (statusList.isEmpty())
            return WorkflowResponse.builder().message("Verification Status Not found").status(400).build();

        return WorkflowResponse.builder().message("Success").status(200).data(statusList).build();

    }

    @Override
    public WorkflowResponse saveSubActivityQuestions(SubActivityQuestionsRequest request) {

        Optional<SubActivity> subActivity = subActivityRepository.findById(request.getSubActivityId());
        if (!subActivity.isPresent())
            return WorkflowResponse.builder().message("SubActivity  Not found").status(400).build();
        List<SubActivityQuestions> allExistingQuestions = subActivityQuestionsRepository.findAll();

        boolean exists = allExistingQuestions.stream()
                .anyMatch(q -> q.getSubActivityId().equals(request.getSubActivityId()));

        if (exists) {
            return WorkflowResponse.builder().message("SubActivity already exists, cannot add duplicate").status(400).build();
        }


        List<Question> allQuestions = questionRepository.findAllById(request.getQuestions());
        if (allQuestions.size() != request.getQuestions().size()) {
            return WorkflowResponse.builder().message("Some questions were not found").status(400).build();
        }

        SubActivityQuestions subActivityQuestions = subActivityQuestionsRepository.save(CallCenterRequestMapper.mapSubActivityQuestions(request));
        return WorkflowResponse.builder().message("SubActivityQuestion saved successfully")
                .status(200).data(subActivityQuestions).build();
    }

    @Override
    public WorkflowResponse getQuestionBySubActivityId(Long subActivityId) {
        SubActivityQuestions subActivityQuestions = subActivityQuestionsRepository.findBySubActivityId(subActivityId);
        if (subActivityQuestions == null)
            return WorkflowResponse.builder().message("SubActivity not not found").status(400).build();
        List<Question> questions = questionRepository.findAllById(subActivityQuestions.getQuestionsIds());
        return WorkflowResponse.builder().message("success").status(200).data(questions).build();
    }

    /*    @Override
    public WorkflowResponse saveCallCenterVerification(CallCenterVerificationRequest request) {
        Optional<CallCenterVerificationStatus> verificationStatus = ccVerificationStatusRepository.findById(request.getVerificationStatusId());
        if (!verificationStatus.isPresent()) return WorkflowResponse.builder().message("invalid verification status").status(400).build();
        Optional<User> user = loginRepository.findById(request.getVerifiedBy());
        if (!user.isPresent()) return WorkflowResponse.builder().message("User not found").status(400).build();
        List<QuestionAnswers> questionAnswersList = new ArrayList<>();
        if (request.getQuestionAnswerList() != null && request.getQuestionAnswerList().size() > 0) {
            List<Integer> questionIds = request.getQuestionAnswerList().stream().map(questionAnswer -> questionAnswer.getQuestionId()).collect(Collectors.toList());
            List<Question> questions = questionRepository.findAllById(questionIds);
            questionAnswersList = populateQuestionAnswers(questions, request.getQuestionAnswerList());
        }
        CallCenterVerification callCenterVerification = ccVerificationRepository.save(CallCenterRequestMapper.mapParticipantVerification(request, questionAnswersList, user.get(),verificationStatus.get()));
        return WorkflowResponse.builder().message("Participant Verification data is saved successfully").status(200).data(callCenterVerification).build();
    }*/
    @Override
    public WorkflowResponse saveCallCenterVerification(CallCenterVerificationRequest request) throws DataException {
        CallCenterVerification callCenterVerification;
        List<QuestionAnswers> questionAnswersList = new ArrayList<>();

        CallCenterVerificationStatus verificationStatus = ccVerificationStatusRepository.findById(request.getVerificationStatusId())
                .orElseThrow(() -> new DataException("Invalid verification status", "VERIFICATION_STATUS_NOT_FOUND", 400));

        User user = loginRepository.findById(request.getVerifiedBy())
                .orElseThrow(() -> new DataException("User details found", "USER_NOT_FOUND", 400));

        if (request.getQuestionAnswerList() != null && !request.getQuestionAnswerList().isEmpty()) {
            List<Integer> questionIds = request.getQuestionAnswerList().
                    stream().map(CallCenterVerificationRequest.QuestionAnswer::getQuestionId).collect(Collectors.toList());

            List<Question> questions = questionRepository.findAllById(questionIds);
            questionAnswersList = populateQuestionAnswers(questions, request.getQuestionAnswerList());
        }

        Optional<CallCenterVerification> existing = ccVerificationRepository.findByProgramIdAndParticipantId(request.getProgramId(), request.getParticipantId());

        if (existing.isPresent()) {
            callCenterVerification = existing.get();
            callCenterVerification.setVerifiedBy(user);
            callCenterVerification.setVerificationDate(DateUtil.stringToDate(request.getVerificationDate(), "dd-MM-yyyy"));
            callCenterVerification.setCcVerificationStatus(verificationStatus);
            callCenterVerification.getQuestionAnswers().clear();
            callCenterVerification.getQuestionAnswers().addAll(questionAnswersList);
        } else {
            callCenterVerification = CallCenterRequestMapper.mapParticipantVerification(request, questionAnswersList, user, verificationStatus);
        }
        CallCenterVerification saved = ccVerificationRepository.save(callCenterVerification);

        return WorkflowResponse.builder()
                .message("Participant Verification data is saved successfully")
                .status(200)
                .data(saved)
                .build();
    }


    @Override
    public WorkflowResponse getAllCallCenterVerificationData() {

        List<CallCenterVerification> callCenterVerificationList = ccVerificationRepository.findAll();
        if (callCenterVerificationList.isEmpty())
            return WorkflowResponse.builder().message("Verification list is empty").status(400).build();
        List<CallCenterVerificationResponse> verificationResponses = callCenterVerificationList.stream().map(callCenterVerification -> CallCenterResponseMapper.mapParticipantVerification(callCenterVerification
                , getVerificationStatusByID(callCenterVerification.getCcVerificationStatus().getCcVerificationStatusId()))).collect(Collectors.toList());
        return WorkflowResponse.builder().message("Success").status(200).data(verificationResponses).build();
    }

    public CallCenterVerificationStatus getVerificationStatusByID(Long participantVerificationStatusId) {
        Optional<CallCenterVerificationStatus> status = ccVerificationStatusRepository.findById(participantVerificationStatusId);
        return status.get();
    }

    private List<QuestionAnswers> populateQuestionAnswers(List<Question> questions, List<CallCenterVerificationRequest.QuestionAnswer> questionAnswerList) {
        Map<Integer, Question> questionMap = questions.stream().collect(Collectors.toMap(Question::getQuestionId, question -> question));
        List<QuestionAnswers> questionAnswersList = new ArrayList<>();
        for (CallCenterVerificationRequest.QuestionAnswer questionAnswer : questionAnswerList) {
            questionAnswersList.add(QuestionAnswers
                    .builder()
                    .question(questionMap.get(questionAnswer.getQuestionId()))
                    .answers(String.join(",", questionAnswer.getAnswers()))
                    .build());
        }
        return questionAnswersList;
    }


}
