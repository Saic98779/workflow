package com.metaverse.workflow.email;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class EmailRequest {

    @Email
    @NotBlank
    private List<String> to;

    @Email
    @NotBlank
    private List<String> cc;

    @NotBlank
    private String subject;

    @NotBlank
    private String body;

    private boolean html = false;
}

