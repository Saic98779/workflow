package com.metaverse.workflow.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailEvent {

    private List<String> to;
    private List<String> cc;
    private String subject;
    private String body;
    private boolean html;
}

