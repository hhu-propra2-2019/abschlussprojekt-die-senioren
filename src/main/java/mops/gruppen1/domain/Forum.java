package mops.gruppen1.domain;

import lombok.Getter;

/**
 * Represents link to an external forum application
 */
@Getter
public class Forum {
    private String link;

    public Forum(String link) {
        this.link = link;
    }

}
