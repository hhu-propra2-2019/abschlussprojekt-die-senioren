package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * change attribute Status in Membership to 'Deactivated'. NO deletion from datastructures
 */
public class MemberDeletionEvent implements Event {

    private String groupId;
    private String removedUserId;
    private String removedByUserId;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {

    }
}
