package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Representation of a user within the domain.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User {
    private Username username;
}
