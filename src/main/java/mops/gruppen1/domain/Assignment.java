package mops.gruppen1.domain;

import lombok.Getter;

/**
 * Represents link to an external assignment application
 */
@Getter
public class Assignment {
    private String link;

    public Assignment(String link) {
        this.link = link;
    }
}
