package mops.gruppen1.domain;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Representing the name of a user.
 * Will be received from KeyCloak Server and is unique.
 */
@Getter
@AllArgsConstructor
public class Username {
    @CsvBindByName
    private String username;

    @Override
    public String toString() {
        return username;
    }
}
