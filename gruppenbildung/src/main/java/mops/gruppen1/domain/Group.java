package mops.gruppen1.domain;

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
@Table(name = "groups1")
public class Group {
    @OneToMany(mappedBy = "group")
    List<Membership> members;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID groupId;
    @Embedded
    private GroupName name;
    @Embedded
    private GroupDescription description;
    @Embedded
    private GroupStatus status;
}
