package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class Group {
    private UUID id;
    private GroupName name;
    private GroupDescription description;
    List<Membership> members;
    private GroupStatus status;
}
