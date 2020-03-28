package mops.gruppen1.domain;

import lombok.Getter;

/**
 * Represents link to an external appointment application
 */
@Getter
public class Appointment {
    private String link;

    public Appointment(String link) {
        this.link = link;
    }
}