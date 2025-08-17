package com.metaverse.workflow.nontraining.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.BindParam;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainingAndNonTrainingDto {
    private List<TrainingProgramDto> trainingProgramDtos;
    private List<NonTrainingProgramDto> nonTrainingProgramDtos;
}
