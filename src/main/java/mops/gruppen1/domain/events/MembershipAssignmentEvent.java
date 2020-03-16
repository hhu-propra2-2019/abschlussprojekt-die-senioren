package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * For public Groups:
 * Create a Membership with Status 'ACTIVE'
 * Add Membership to groupToMembers and userToMembers
 */
public class MembershipAssignmentEvent implements Event {

    private UUID groupId;
    private Username userName;
    private Type membershipType;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashSet<User> users, HashSet<Group> groups) {

    }
}
