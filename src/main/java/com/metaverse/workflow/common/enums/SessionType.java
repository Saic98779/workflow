package com.metaverse.workflow.common.enums;


public enum SessionType {

    SESSION("Session", new String[]{"Lecture", "Discussion", "Felicitation"}),
    BREAK("Break", new String[]{"Break", "Lunch", "Dinner"});

    SessionType(String name, String[] methodology) {
        this.name = name;
        this.methodology = methodology;
    }

    private String name;

    private String[] methodology;

    public String getName() {
        return name;
    }

    public String[] getMethodology() {
        return methodology;
    }

}
