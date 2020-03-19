package mops.gruppen1.domain;

import lombok.Getter;

/**
 * Represents link to an external material application
 */
@Getter
public class Material {
    private String link;

    public Material(String link) {
        this.link = link;
    }
}
