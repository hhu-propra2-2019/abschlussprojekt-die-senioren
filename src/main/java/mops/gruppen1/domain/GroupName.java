package mops.gruppen1.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representation of a group - name.
 */
@AllArgsConstructor
@Getter
public class GroupName {
    private String name;

    @Override
    public String toString() {
        return name;
    }
}
