package com.metaverse.workflow.aleap_handholding.response_dto;

import com.metaverse.workflow.model.aleap_handholding.FeasibilityInput;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FeasibilityInputResponse {

    private Long feasibilityInputId;
    private Long marketStudyId;
    private String dateOfStudy;
    private String inputDetails;
    private FeasibilityInput.SourceType source;
    private String sector;
    private String feasibilityActivity;
}
