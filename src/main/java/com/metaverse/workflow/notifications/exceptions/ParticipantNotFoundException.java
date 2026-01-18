package com.metaverse.workflow.notifications.exceptions;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(String message) {
        super(message);
    }
}
