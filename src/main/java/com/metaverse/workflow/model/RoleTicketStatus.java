package com.metaverse.workflow.model;

import com.metaverse.workflow.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_ticket_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleTicketStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // REQUIRED PRIMARY KEY

    private String roleName;

    @Enumerated(EnumType.STRING)
    private TicketStatus statusName;
}
