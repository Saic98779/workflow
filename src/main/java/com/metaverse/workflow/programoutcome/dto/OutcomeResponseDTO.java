package com.metaverse.workflow.programoutcome.dto;

import com.metaverse.workflow.programoutcome.service.OutcomeDetails;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutcomeResponseDTO {
    private List<OutcomeDetails.OutcomeDataSet> headers;
    private List<?> body; // can hold any type (ONDCRegistrationDTO, other DTOs)
}
