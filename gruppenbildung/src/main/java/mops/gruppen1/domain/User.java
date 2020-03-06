package mops.gruppen1.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
//@Table(name = "users")
public class User {
    @OneToMany(mappedBy = "user")
    List<Membership> members;
    @Id
    @GeneratedValue
    private UUID userId;
    private String name;
}
