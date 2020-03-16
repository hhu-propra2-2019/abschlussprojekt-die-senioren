package mops.gruppen1.domain.events;

import mops.gruppen1.applicationService.GroupService;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * All Event classes are implementing the execute method, but do not necessarily need all parameters
 */
public interface Event {
    void execute(HashMap<Group, List<Membership>> groupToMembers,
                        HashMap<User, List<Membership>> userToMembers,
                        HashSet<User> users, HashSet<Group> groups);
}
