package mops.gruppen1.domain;

import lombok.Getter;

@Getter
public class Appointment {
    String link;

    public Appointment(String link) {
        this.link = link;
    }
}
