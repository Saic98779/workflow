package com.metaverse.workflow.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="question_answers")
public class QuestionAnswers {
    @Id
    @Column(name="question_answers_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer questionAnswersId;

    @ManyToMany(mappedBy = "questionAnswers", fetch = FetchType.LAZY)
    @JsonIgnore
    @JsonBackReference
    private List<CallCenterVerification> callCenterVerifications;

    @ManyToOne
    @JoinColumn(name="question_id")
    private Question question;

    @Column(name = "answer_value")
    private String answers ;

}