package mops.gruppen1.applicationService;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.domain.*;
import mops.gruppen1.domain.events.*;
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

    private ValidationResult isGroupActive(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean isActive = group.getGroupStatus().equals(GroupStatus.ACTIVE);
        if (isActive) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe ist nicht aktiv.");
        return validationResult;
    }

    private ValidationResult isPublic(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean isPublic = group.getGroupType().equals(GroupType.PUBLIC);
        if (isPublic) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe ist nicht öffentlich.");
        return validationResult;
    }

    private ValidationResult isRestricted(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean isRestricted = group.getGroupType().equals(GroupType.RESTRICTED);
        if (isRestricted) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe ist nicht zugangsbeschränkt.");
        return validationResult;
    }

    private ValidationResult isAdmin(String userName, String groupId, ValidationResult validationResult) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);
        Membership membership = getMembership(memberships, group);

        boolean isAdmin = membership.getMembershipType().equals(MembershipType.ADMIN);
        if (isAdmin) {
            return validationResult;
        }
        validationResult.addError("Der Nutzer ist kein Administrator der Gruppe.");
        return validationResult;
    }

    private ValidationResult membershipIsActive(String userName, String groupId, ValidationResult validationResult) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);
        Membership membership = getMembership(memberships, group);

        boolean isActive = membership.getMembershipStatus().equals(MembershipStatus.ACTIVE);
        if (isActive) {
            return validationResult;
        }
        validationResult.addError("Der Nutzer ist kein aktives Mitglied der Gruppe.");
        return validationResult;
    }

    private ValidationResult membershipIsPending(String userName, String groupId, ValidationResult validationResult) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);
        Membership membership = getMembership(memberships, group);
        boolean isPending = membership.getMembershipStatus().equals(MembershipStatus.PENDING);
        if (isPending) {
            return validationResult;
        }
        validationResult.addError("Die Mitgliedschaftsanfrage existiert nicht.");
        return validationResult;
    }

    private ValidationResult isMember(String userName, String groupId, ValidationResult validationResult) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        boolean isMember = getMembership(memberships, group) != null;
        if (isMember) {
            return validationResult;
        }
        validationResult.addError("Der Nutzer ist nicht Mitglied der Gruppe.");
        return validationResult;
    }
    private ValidationResult isNotMember(String userName, String groupId, ValidationResult validationResult) {
        User user = users.get(userName);
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        boolean isNotMember = getMembership(memberships, group) == null;
        if (isNotMember) {
            return validationResult;
        }
        validationResult.addError("Der Nutzer ist bereits Mitglied der Gruppe.");
        return validationResult;
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

    public void assignMembership(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        ValidationResult validationResult = new ValidationResult();
        validationResult = isPublic(groupId, validationResult);
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = isNotMember(userName, groupId, validationResult);

        try {
            performMembershipAssignmentEvent(userName, groupId, membershipType);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipAssignmentEvent(String userName, String groupId, String membershipType){
        MembershipAssignmentEvent membershipAssignmentEvent = new MembershipAssignmentEvent(groupId, userName, membershipType);
        membershipAssignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipAssignmentEventDTO = events.createEventDTO(userName, groupId, timestamp, "MembershipAssignmentEvent", membershipAssignmentEvent);

        events.saveToRepository(membershipAssignmentEventDTO);
    }

    public void requestMembership(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        ValidationResult validationResult = new ValidationResult();
        validationResult = isRestricted(groupId, validationResult);
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = isNotMember(userName, groupId, validationResult);

        try {
            performMembershipRequestEvent(userName, groupId, membershipType);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipRequestEvent(String userName, String groupId, String membershipType){
        MembershipRequestEvent membershipRequestEvent = new MembershipRequestEvent(groupId, userName, membershipType);
        membershipRequestEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipRequestEventDTO = events.createEventDTO(userName, groupId, timestamp, "MembershipRequestEvent", membershipRequestEvent);

        events.saveToRepository(membershipRequestEventDTO);
    }

    public void resignMembership(String userName, String groupId) {
        ValidationResult validationResult = new ValidationResult();
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = isMember(userName, groupId, validationResult);
        validationResult = membershipIsActive(userName, groupId, validationResult);

        try {
            performMembershipResignmentEvent(userName, groupId);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipResignmentEvent(String userName, String groupId) {
        MemberResignmentEvent memberResignmentEvent = new MemberResignmentEvent(groupId, userName);
        memberResignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO memberResignmentEventDTO = events.createEventDTO(userName, groupId, timestamp, "MemberResignmentEvent", memberResignmentEvent);

        events.saveToRepository(memberResignmentEventDTO);
    }


    public void rejectMembership(String userName, String groupId) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = isRestricted(groupId, validationResult);
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = membershipIsPending(userName, groupId, validationResult);

        try {
            performMembershipRejectEvent(userName, groupId);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipRejectEvent(String userName, String groupId) {
        MembershipRejectionEvent membershipRejectionEvent = new MembershipRejectionEvent(groupId, userName);
        membershipRejectionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipRejectionEventDTO = events.createEventDTO(userName, groupId, timestamp, "MembershipRejectionEvent", membershipRejectionEvent);

        events.saveToRepository(membershipRejectionEventDTO);
    }

    public void deleteMembership(String userName, String groupId, String deletedBy) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = membershipIsActive(userName, groupId, validationResult);
        validationResult = membershipIsActive(deletedBy, groupId, validationResult);
        validationResult = isAdmin(deletedBy, groupId, validationResult);

        try {
            performMembershipDeletionEvent(userName, groupId, deletedBy);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipDeletionEvent(String userName, String groupId, String deletedBy) {
        MemberDeletionEvent memberDeletionEvent = new MemberDeletionEvent(groupId, userName,deletedBy);
        memberDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipDeletionEventDTO = events.createEventDTO(userName, groupId, timestamp, "MemberDeletionEvent", memberDeletionEvent);

        events.saveToRepository(membershipDeletionEventDTO);
    }

    public void updateMembership(String userName, String groupId, String updatedBy, String updatedTo) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = membershipIsActive(userName, groupId, validationResult);
        validationResult = membershipIsActive(updatedBy, groupId, validationResult);
        validationResult = isAdmin(updatedBy, groupId, validationResult);

        try {
            performMembershipUpdateEvent(userName, groupId, updatedBy, updatedTo);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipUpdateEvent(String userName, String groupId, String deletedBy, String updatedTo) {
        MembershipUpdateEvent membershipUpdateEvent = new MembershipUpdateEvent(groupId, userName,deletedBy, updatedTo);
        membershipUpdateEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipUpdateEventDTO = events.createEventDTO(userName, groupId, timestamp, "MembershipUpdateEvent", membershipUpdateEvent);

        events.saveToRepository(membershipUpdateEventDTO);
    }

    public void acceptMembership(String userName, String groupId) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = isRestricted(groupId, validationResult);
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = membershipIsPending(userName, groupId, validationResult);

        try {
            performMembershipAcceptanceEvent(userName, groupId);
        }
        catch (RuntimeException exception){}
    }

    private void performMembershipAcceptanceEvent(String userName, String groupId){
        MembershipAcceptanceEvent membershipAcceptanceEvent = new MembershipAcceptanceEvent(groupId, userName);
        membershipAcceptanceEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipAcceptanceEventDTO = events.createEventDTO(userName, groupId, timestamp, "MembershipAcceptanceEvent", membershipAcceptanceEvent);

        events.saveToRepository(membershipAcceptanceEventDTO);
    }

    public void deleteGroup(String userName, String groupId) {
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, userName);
        groupDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupDeletionEventDTO = events.createEventDTO(userName, groupId, timestamp, "GroupDeletionEvent", groupDeletionEvent);

        events.saveToRepository(groupDeletionEventDTO);

    }

}
