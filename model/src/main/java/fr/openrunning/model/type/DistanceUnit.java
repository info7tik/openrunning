package fr.openrunning.model.type;

import lombok.Getter;

public enum DistanceUnit {
    METER("m"), KILOMETER("km"), YARD("yard"), MILE("mile");

    @Getter
    private final String jsonString;

    private DistanceUnit(String jsonString){
        this.jsonString = jsonString;
    }
}
