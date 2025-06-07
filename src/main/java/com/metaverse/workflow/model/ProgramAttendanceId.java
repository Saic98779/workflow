package com.metaverse.workflow.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Embeddable
public class ProgramAttendanceId {

    @Column(name = "program_id", nullable = false)
    private Long programId;
    @Column(name = "participant_id", nullable = false)
    private Long participantId;

}
