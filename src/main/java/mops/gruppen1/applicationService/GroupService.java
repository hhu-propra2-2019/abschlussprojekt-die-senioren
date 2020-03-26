package mops.gruppen1.applicationService;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipStatus;
import mops.gruppen1.domain.User;
import mops.gruppen1.domain.events.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service to manage the group entities
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@Service
public class GroupService {

    EventService events;
    CheckService checkService;
    private HashMap<String, List<Membership>> groupToMembers;
    private HashMap<String, List<Membership>> userToMembers;
    private HashMap<String, Group> groups;
    private HashMap<String, User> users;
    private String lastCreatedGroup;

    @Autowired
    public GroupService(EventService eventService, CheckService checkService) {
        this.events = eventService;
        this.checkService = checkService;
        this.groupToMembers = new HashMap<>();
        userToMembers = new HashMap<>();
        groups = new HashMap<>();
        users = new HashMap<>();
    }

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
        try {
            performGroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator, groupType);
        } catch (Exception e) {
            validationResult.addError("Unexpected failure.");
        }
        return validationResult;
    }

    void performGroupCreationEvent(String groupDescription, String groupName, String groupCourse, String groupCreator, String groupType) {
        GroupCreationEvent groupCreationEvent = new GroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator, groupType);
        groupCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(groupCreator, groupCreationEvent.getGroupId(), "GroupCreationEvent", groupCreationEvent);
        this.lastCreatedGroup = groupCreationEvent.getGroupId();
    }


    public ValidationResult deleteGroup(String groupId, String userName) {
        List<ValidationResult> validationResults = new ArrayList<ValidationResult>();
        validationResults.add(checkService.isAdmin(userName, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        ValidationResult validationResult = collectCheck(validationResults);
        if (validationResult.isValid()) {
            try {
                performGroupDeletionEvent(userName, groupId);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    void performGroupDeletionEvent(String userName, String groupId) {
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, userName);
        groupDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "GroupDeletionEvent", groupDeletionEvent);
    }

    public ValidationResult updateGroupProperties(String groupId, String updatedBy, String groupName, String description, String groupType) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isAdmin(updatedBy, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        ValidationResult validationResult = collectCheck(validationResults);
        if (validationResult.isValid()) {
            try {
                performGroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    void performGroupPropertyUpdateEvent(String groupId, String updatedBy, String groupName, String description, String groupType) {
        GroupPropertyUpdateEvent groupPropertyUpdateEvent = new GroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);
        groupPropertyUpdateEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(updatedBy, groupId, "GroupPropertyUpdateEvent", groupPropertyUpdateEvent);
    }

    public ValidationResult createUser(String userName) {
        ValidationResult validationResult = checkService.doesUserExist(userName, users);
        try {
            performUserCreationEvent(userName);
        } catch (Exception e) {
            validationResult.addError("Unexpected failure.");
        }
        return validationResult;
    }

    void performUserCreationEvent(String userName) {
        UserCreationEvent userCreationEvent = new UserCreationEvent(userName);
        userCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        //TODO is there a user that creates other users?
        persistEvent(userCreationEvent.getUsername(), null, "UserCreationEvent", userCreationEvent);
    }

    public ValidationResult assignMembershipToPublicGroup(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isPublic(groupId, groups));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isNotMember(userName, groupId, groups, users, userToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipAssignmentEvent(userName, groupId, membershipType);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    public ValidationResult assignMembershipToRestrictedGroup(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isRestricted(groupId, groups));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isNotMember(userName, groupId, groups, users, userToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipAssignmentEvent(userName, groupId, membershipType);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }


    void performMembershipAssignmentEvent(String userName, String groupId, String membershipType) {
        MembershipAssignmentEvent membershipAssignmentEvent = new MembershipAssignmentEvent(groupId, userName, membershipType);
        membershipAssignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipAssignmentEvent", membershipAssignmentEvent);
    }

    public ValidationResult requestMembership(String userName, String groupId, String membershipType) {
        /*
            TODO check if group is assigned to a module/course, user has to be assigned to it as well
         */
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isRestricted(groupId, groups));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isNotMember(userName, groupId, groups, users, userToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipRequestEvent(userName, groupId, membershipType);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    void performMembershipRequestEvent(String userName, String groupId, String membershipType) {
        MembershipRequestEvent membershipRequestEvent = new MembershipRequestEvent(groupId, userName, membershipType);
        membershipRequestEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipRequestEvent", membershipRequestEvent);
    }

    public ValidationResult resignMembership(String userName, String groupId) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isMember(userName, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isMembershipActive(userName, groupId, groups, users, userToMembers));
        validationResults.add(checkService.activeAdminRemains(userName, groupId, groupToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipResignmentEvent(userName, groupId);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    void performMembershipResignmentEvent(String userName, String groupId) {
        MemberResignmentEvent memberResignmentEvent = new MemberResignmentEvent(groupId, userName);
        memberResignmentEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipResignmentEvent", memberResignmentEvent);
    }

    public ValidationResult rejectMembership(String userName, String groupId, String rejectedBy) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isRestricted(groupId, groups));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isAdmin(rejectedBy, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isMembershipPending(userName, groupId, groups, users, userToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipRejectEvent(userName, groupId, rejectedBy);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    void performMembershipRejectEvent(String userName, String groupId, String rejectedBy) {
        MembershipRejectionEvent membershipRejectionEvent = new MembershipRejectionEvent(groupId, userName, rejectedBy);
        membershipRejectionEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipRejectionEvent", membershipRejectionEvent);
    }

    public ValidationResult deleteMembership(String userName, String groupId, String deletedBy) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isMembershipActive(userName, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isMembershipActive(deletedBy, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isAdmin(deletedBy, groupId, groups, users, userToMembers));
        validationResults.add(checkService.activeAdminRemains(deletedBy, groupId, groupToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipDeletionEvent(userName, groupId, deletedBy);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }


    void performMembershipDeletionEvent(String userName, String groupId, String deletedBy) {
        MemberDeletionEvent memberDeletionEvent = new MemberDeletionEvent(groupId, userName, deletedBy);
        memberDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipDeletionEvent", memberDeletionEvent);
    }

    public ValidationResult updateMembership(String userName, String groupId, String updatedBy, String updatedTo) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isMembershipActive(userName, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isMembershipActive(updatedBy, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isAdmin(updatedBy, groupId, groups, users, userToMembers));
        validationResults.add(checkService.activeAdminRemains(updatedBy, groupId, groupToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipUpdateEvent(userName, groupId, updatedBy, updatedTo);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }

    void performMembershipUpdateEvent(String userName, String groupId, String deletedBy, String updatedTo) {
        MembershipUpdateEvent membershipUpdateEvent = new MembershipUpdateEvent(groupId, userName, deletedBy, updatedTo);
        membershipUpdateEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipUpdateEvent", membershipUpdateEvent);
    }

    public ValidationResult acceptMembership(String userName, String groupId, String acceptedBy) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults.add(checkService.isRestricted(groupId, groups));
        validationResults.add(checkService.isGroupActive(groupId, groups));
        validationResults.add(checkService.isMembershipPending(userName, groupId, groups, users, userToMembers));
        validationResults.add(checkService.isAdmin(acceptedBy, groupId, groups, users, userToMembers));
        ValidationResult validationResult = collectCheck(validationResults);

        if (validationResult.isValid()) {
            try {
                performMembershipAcceptanceEvent(userName, groupId, acceptedBy);
            } catch (Exception e) {
                validationResult.addError("Unexpected failure.");
            }
        }
        return validationResult;
    }


    void performMembershipAcceptanceEvent(String userName, String groupId, String acceptedBy) {
        MembershipAcceptanceEvent membershipAcceptanceEvent = new MembershipAcceptanceEvent(groupId, userName, acceptedBy);
        membershipAcceptanceEvent.execute(groupToMembers, userToMembers, users, groups);

        persistEvent(userName, groupId, "MembershipAcceptanceEvent", membershipAcceptanceEvent);
    }

    void persistEvent(String userName, String groupId, String eventType, IEvent event) {
        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipAcceptanceEventDTO = events.createEventDTO(userName, groupId, timestamp, eventType, event);

        events.saveToRepository(membershipAcceptanceEventDTO);
    }

    ValidationResult collectCheck(List<ValidationResult> checks) {
        ValidationResult validationResult = new ValidationResult();
        for (ValidationResult check : checks) {
            if (!check.isValid()) {
                validationResult.addError(String.join(" ", check.getErrorMessages()));
            }
        }
        return validationResult;
    }

    public List<Membership> getMembersOfGroup(String groupId) {
        List<Membership> memberships = groupToMembers.get(groupId);
        return memberships;
    }

    public List<Membership> getActiveMembersOfGroup(String groupId) {
        List<Membership> memberships = groupToMembers.get(groupId);
        List<Membership> activeMemberships = memberships.stream()
                .filter(m -> m.getMembershipStatus().equals(MembershipStatus.ACTIVE)).collect(Collectors.toList());
        return activeMemberships;
    }

    public List<Group> getGroupsOfUser(String userName) {
        List<Membership> memberships = userToMembers.get(userName);
        List<Group> groups = memberships.stream().map(member -> member.getGroup()).collect(Collectors.toList());
        return groups;
    }

    public List<Group> getGroupsWhereUserIsActive(String userName) {
        List<Membership> memberships = userToMembers.get(userName);
        List<Group> groupsWhereUserIsActive = memberships.stream()
                .filter(member -> member.getMembershipStatus().equals(MembershipStatus.ACTIVE))
                .map(member -> member.getGroup()).collect(Collectors.toList());
        return groupsWhereUserIsActive;
    }

    public List<Membership> getPendingMemberships(String groupId) {
        List<Membership> memberships = groupToMembers.get(groupId);
        List<Membership> pendingMemberships = memberships.stream()
                .filter(membership -> membership.getMembershipStatus().equals(MembershipStatus.PENDING))
                .collect(Collectors.toList());
        return pendingMemberships;
    }

    public long countPendingRequestOfGroup(String groupId) {
        List<Membership> memberships = groupToMembers.get(groupId);
        long pendingMemberships = memberships.stream()
                .filter(membership -> membership.getMembershipStatus().equals(MembershipStatus.PENDING))
                .count();
        return pendingMemberships;
    }

    public List<Membership> getMembershipsOfUser(String userName) {
        List<Membership> memberships = userToMembers.get(userName);
        return memberships;
    }

    public List<Membership> getActiveMembershipsOfUser(String userName) {
        List<Membership> memberships = userToMembers.get(userName);
        List<Membership> activeMemberships = memberships.stream()
                .filter(membership -> membership.getMembershipStatus().equals(MembershipStatus.ACTIVE))
                .collect(Collectors.toList());
        return activeMemberships;
    }
}


