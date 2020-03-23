package mops.gruppen1.applicationService;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import mops.gruppen1.domain.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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
    @Autowired
    private CheckService checkService;
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

    /**
     * calls performGroupCreationEvent to create, execute and save GroupCreationEvent.
     * Does not include a real validation check because one cannot search for
     * non-existant groups and every user can create groups.
     *
     * @param groupDescription
     * @param groupName
     * @param groupCourse
     * @param groupCreator
     * @param groupType
     * @return ValidationResult (always successful)
     */
    public ValidationResult createGroup(String groupDescription, String groupName, String groupCourse, String groupCreator, String groupType) {
        ValidationResult validationResult = new ValidationResult();
        performGroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator, groupType);
        return validationResult;
    }

    private void performGroupCreationEvent(String groupDescription, String groupName, String groupCourse, String groupCreator, String groupType) {
        GroupCreationEvent groupCreationEvent = new GroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator, groupType);
        groupCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(groupCreator, null, "GroupCreationEvent", groupCreationEvent);
    }

    public ValidationResult deleteGroup(String groupId, String userName) {
        ValidationResult validationResult = checkService.isAdmin(userName, groupId, groups, users, userToMembers, new ValidationResult());
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        if (validationResult.isValid()) {
            performGroupDeletionEvent(userName, groupId);
            return validationResult;
        }
        return validationResult;
    }

    private void performGroupDeletionEvent(String userName, String groupId) {
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, userName);
        groupDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "GroupDeletionEvent", groupDeletionEvent);
    }

    public ValidationResult updateGroupProperties(String groupId, String updatedBy, String groupName, String description, String groupType) {
        ValidationResult validationResult = checkService.isAdmin(updatedBy, groupId, groups, users, userToMembers, new ValidationResult());
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        if (validationResult.isValid()) {
            performGroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);
            return validationResult;
        }
        return validationResult;
    }

    private void performGroupPropertyUpdateEvent(String groupId, String updatedBy, String groupName, String description, String groupType) {
        GroupPropertyUpdateEvent groupPropertyUpdateEvent = new GroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);
        groupPropertyUpdateEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(updatedBy, groupId, "GroupPropertyUpdateEvent", groupPropertyUpdateEvent);
    }

    public ValidationResult createUser(String userName) {
        ValidationResult validationResult = checkService.doesUserExist(userName, users, new ValidationResult());
        performUserCreationEvent(userName);
        return validationResult;
    }

    private void performUserCreationEvent(String userName) {
        UserCreationEvent userCreationEvent = new UserCreationEvent(userName);
        userCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        //TODO is there a user that creates other users?
        persistEvent(null, null, "UserCreationEvent", userCreationEvent);
    }

    public ValidationResult assignMembership(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isPublic(groupId, groups, validationResult);
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.isNotMember(userName, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipAssignmentEvent(userName, groupId, membershipType);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }

    private void performMembershipAssignmentEvent(String userName, String groupId, String membershipType) {
        MembershipAssignmentEvent membershipAssignmentEvent = new MembershipAssignmentEvent(groupId, userName, membershipType);
        membershipAssignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipAssignmentEvent", membershipAssignmentEvent);
    }

    public ValidationResult requestMembership(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isRestricted(groupId, groups, validationResult);
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.isNotMember(userName, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipRequestEvent(userName, groupId, membershipType);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }

    private void performMembershipRequestEvent(String userName, String groupId, String membershipType) {
        MembershipRequestEvent membershipRequestEvent = new MembershipRequestEvent(groupId, userName, membershipType);
        membershipRequestEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipRequestEvent", membershipRequestEvent);
    }

    public ValidationResult resignMembership(String userName, String groupId) {
        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.isMember(userName, groupId, groups, users, userToMembers, validationResult);
        validationResult = checkService.membershipIsActive(userName, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipResignmentEvent(userName, groupId);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }

    private void performMembershipResignmentEvent(String userName, String groupId) {
        MemberResignmentEvent memberResignmentEvent = new MemberResignmentEvent(groupId, userName);
        memberResignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipResignmentEvent", memberResignmentEvent);
    }

    public ValidationResult rejectMembership(String userName, String groupId) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isRestricted(groupId, groups, validationResult);
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.membershipIsPending(userName, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipRejectEvent(userName, groupId);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }

    private void performMembershipRejectEvent(String userName, String groupId) {
        MembershipRejectionEvent membershipRejectionEvent = new MembershipRejectionEvent(groupId, userName);
        membershipRejectionEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipRejectionEvent", membershipRejectionEvent);
    }

    public ValidationResult deleteMembership(String userName, String groupId, String deletedBy) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.membershipIsActive(userName, groupId, groups, users, userToMembers, validationResult);
        validationResult = checkService.membershipIsActive(deletedBy, groupId, groups, users, userToMembers, validationResult);
        validationResult = checkService.isAdmin(deletedBy, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipDeletionEvent(userName, groupId, deletedBy);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }


    private void performMembershipDeletionEvent(String userName, String groupId, String deletedBy) {
        MemberDeletionEvent memberDeletionEvent = new MemberDeletionEvent(groupId, userName, deletedBy);
        memberDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipDeletionEvent", memberDeletionEvent);
    }

    public ValidationResult updateMembership(String userName, String groupId, String updatedBy, String updatedTo) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.membershipIsActive(userName, groupId, groups, users, userToMembers, validationResult);
        validationResult = checkService.membershipIsActive(updatedBy, groupId, groups, users, userToMembers, validationResult);
        validationResult = checkService.isAdmin(updatedBy, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipUpdateEvent(userName, groupId, updatedBy, updatedTo);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }

    private void performMembershipUpdateEvent(String userName, String groupId, String deletedBy, String updatedTo) {
        MembershipUpdateEvent membershipUpdateEvent = new MembershipUpdateEvent(groupId, userName, deletedBy, updatedTo);
        membershipUpdateEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipUpdateEvent", membershipUpdateEvent);
    }

    public ValidationResult acceptMembership(String userName, String groupId) {

        ValidationResult validationResult = new ValidationResult();
        validationResult = checkService.isRestricted(groupId, groups, validationResult);
        validationResult = checkService.isGroupActive(groupId, groups, validationResult);
        validationResult = checkService.membershipIsPending(userName, groupId, groups, users, userToMembers, validationResult);

        if (validationResult.isValid()) {
            try {
                performMembershipAcceptanceEvent(userName, groupId);
            } catch (RuntimeException exception) {
                validationResult.addError("Unexpected failure");
            }
        }
        return validationResult;
    }


    private void performMembershipAcceptanceEvent(String userName, String groupId) {
        MembershipAcceptanceEvent membershipAcceptanceEvent = new MembershipAcceptanceEvent(groupId, userName);
        membershipAcceptanceEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipAcceptanceEvent", membershipAcceptanceEvent);
    }

    private void persistEvent(String userName, String groupId, String eventType, IEvent event) {
        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipAcceptanceEventDTO = events.createEventDTO(userName, groupId, timestamp, eventType, event);

        events.saveToRepository(membershipAcceptanceEventDTO);
    }
}


