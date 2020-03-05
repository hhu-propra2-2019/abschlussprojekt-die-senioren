package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Group {
    @Id
    @GeneratedValue
    private UUID id;
    private GroupName name;
    private GroupDescription description;
    List<Membership> members;
    private GroupStatus status;
}
