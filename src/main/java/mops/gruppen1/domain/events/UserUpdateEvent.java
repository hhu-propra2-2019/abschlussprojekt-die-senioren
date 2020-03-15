package mops.gruppen1.domain.events;

import mops.gruppen1.applicationService.GroupService;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * DELETE THIS EVENT?
 * - Only needed if we add another attribute to class User, e.g. displayed name or full name.
 */
public class UserUpdateEvent implements Event {


    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashSet<User> users, HashSet<Group> groups) {

    }
}
