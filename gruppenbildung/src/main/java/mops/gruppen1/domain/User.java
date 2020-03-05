package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String name;
}
