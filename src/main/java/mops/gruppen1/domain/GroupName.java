package mops.gruppen1.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

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
