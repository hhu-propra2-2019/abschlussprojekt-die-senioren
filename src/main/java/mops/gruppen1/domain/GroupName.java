package mops.gruppen1.domain;


import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Representation of a group - name.
 */
@AllArgsConstructor
public class GroupName {
    private String name;


    @Override
    public String toString() {
        return name;
    }
}
