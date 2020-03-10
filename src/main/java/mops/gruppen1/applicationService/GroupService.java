package mops.gruppen1.applicationService;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import mops.gruppen1.domain.Username;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Service to manage the group entities
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class GroupService {
    HashMap<Group, List<Membership>> groupToMembers;
    HashMap<User, List<Membership>> userToMembers;
    HashSet<User> users;
    HashSet<Group> groups;
}
