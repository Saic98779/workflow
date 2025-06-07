package com.metaverse.workflow.callcenter.service;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Setter
@Getter
public class QuestionRequest {

    private String questionFieldType;
    private String question;
    private List<String> options ;

}
