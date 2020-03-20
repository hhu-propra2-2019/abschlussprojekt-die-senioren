package mops.gruppen1.applicationService;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.domain.*;
import mops.gruppen1.domain.events.GroupCreationEvent;
import mops.gruppen1.domain.events.GroupDeletionEvent;
import mops.gruppen1.domain.events.IEvent;
import mops.gruppen1.domain.events.MembershipAssignmentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Service to manage the group entities
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Service
public class GroupService {
    @Autowired //TODO: Remove Autowired ?
            EventService events;
    private HashMap<String, List<Membership>> groupToMembers;
    private HashMap<String, List<Membership>> userToMembers;
    private HashMap<String, Group> groups;
    private HashMap<String, User> users;

    public void init() {
        events.loadEvents();
        List<IEvent> eventList = events.getEvents();
        eventList.stream().forEach(e -> e.execute(
                groupToMembers,
                userToMembers,
                users,
                groups
        ));
    }

    public void createGroup() {

    }

    private boolean isGroupActive(String groupId) {
        Group group = groups.get(groupId);
        boolean isActive = group.getGroupStatus().equals(GroupStatus.ACTIVE);
        return isActive;
    }

    private boolean isPublic(String groupId) {
        Group group = groups.get(groupId);
        boolean isPublic = group.getGroupType().equals(GroupType.PUBLIC);
        return isPublic;
    }

    private boolean isRestricted(String groupId) {
        Group group = groups.get(groupId);
        boolean isRestricted = group.getGroupType().equals(GroupType.RESTRICTED);
        return isRestricted;
    }

    private boolean isAdmin(String userName, String groupId) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);
        Membership membership = getMembership(memberships, group);

        boolean isAdmin = membership.getType().equals(Type.ADMIN);
        return isAdmin;
    }

    private boolean isActive(String userName, String groupId) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);
        Membership membership = getMembership(memberships, group);

        boolean isActive = membership.getStatus().equals(Status.ACTIVE);
        return isActive;
    }

    private boolean isPending(String userName, String groupId) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);
        Membership membership = getMembership(memberships, group);
        boolean isPending = membership.getStatus().equals(Status.PENDING);
        return isPending;
    }

    private boolean isMember(String userName, String groupId) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        boolean isMember = getMembership(memberships, group) != null;
        return isMember;
    }

    private Membership getMembership(List<Membership> memberships, Group group) {
        Membership membership = null;
        for (Membership m : memberships) {
            if (m.getGroup().equals(group)) {
                membership = m;
                break;
            }
        }
        return membership;
    }

    public void performGroupCreationEvent(String groupDescription, String groupName, String groupCourse, String groupCreator, String groupType) {
        GroupCreationEvent groupCreationEvent = new GroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator, groupType);
        groupCreationEvent.execute(groupToMembers, userToMembers, users, groups);
        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupCreationEventDTO = events.createEventDTO(groupCreator, groupCreationEvent.getGroupID(), timestamp, "GroupCreationEvent", groupCreationEvent);
        events.saveToRepository(groupCreationEventDTO);
    }

    public void assignMembership(String userName, Group group, String membershipType) {


        /* todo check if GroupType is PUBLIC and GroupStatus is 'active'
            todo check if group is assigned to a module/course, user has to be assigned to it as well
             todo check if user is already a member of the group
             todo check if group is part of hashmaps
         */
        String groupID = group.getGroupId().toString();

        MembershipAssignmentEvent membershipAssignmentEvent = new MembershipAssignmentEvent(groupID, userName, membershipType);
        membershipAssignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipAssignmentEventDTO = events.createEventDTO(userName, groupID, timestamp, "MembershipAssignmentEvent", membershipAssignmentEvent);

        events.saveToRepository(membershipAssignmentEventDTO);
    }

    public void deleteGroup(String userName, UUID groupID) {
        String groupId = groupID.toString();
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, userName);
        groupDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupDeletionEventDTO = events.createEventDTO(userName, groupId, timestamp, "GroupDeletionEvent", groupDeletionEvent);

        events.saveToRepository(groupDeletionEventDTO);

    }

}
