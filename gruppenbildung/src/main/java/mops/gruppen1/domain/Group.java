package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Group {
    @Id
    @GeneratedValue
    private UUID id;
    private GroupName name;
    private GroupDescription description;
    @OneToMany
    List<Membership> members;
    private GroupStatus status;
}
