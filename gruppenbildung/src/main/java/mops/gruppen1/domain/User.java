package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
}
