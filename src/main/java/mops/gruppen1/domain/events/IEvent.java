package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * All Event classes are implementing the execute method, but do not necessarily need all parameters.
 */
public interface IEvent {
    void execute(HashMap<String, List<Membership>> groupToMembers,
                 HashMap<String, List<Membership>> userToMembers,
                 HashMap<String, User> users, HashMap<String, Group> groups);
}
