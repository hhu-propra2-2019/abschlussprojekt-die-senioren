package mops.gruppen1.domain;

import java.util.List;
import java.util.UUID;


public abstract class Group {
    List<Membership> members;
    UUID groupId;
    GroupName name;
    GroupDescription description;
}
