package com.metaverse.workflow.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sub_activity_questions")
public class SubActivityQuestions {
    @Id
    @Column(name="sub_activity_question_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer subActivityQuestionsId;

    @Column(name="sub_activity_id")
    private Long subActivityId;


    private List<Integer> questionsIds;

    @Column(name="created_on",insertable = true,updatable = false)
    @CreationTimestamp
    private Date createdOn;
    @Column(name="updated_on",insertable = false,updatable = true)
    @UpdateTimestamp
    private Date updatedOn;
}
