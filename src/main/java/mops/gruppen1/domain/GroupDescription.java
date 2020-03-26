package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representation of a group - description.
 */
@AllArgsConstructor
@Getter
public class GroupDescription {
    private String description;

    @Override
    public String toString() {
        return description;
    }
}
