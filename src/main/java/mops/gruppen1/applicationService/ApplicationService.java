package mops.gruppen1.applicationService;

import lombok.Getter;
import mops.gruppen1.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service that controls calls to GroupService
 */
@Service
@Getter
public class ApplicationService {
    @Autowired
    public GroupService groupService;

    /**
     * method returns all members belonging to the requested group
     *
     * @param groupId
     * @return List of Memberships
     */
    public List<Membership> getMembersOfGroup(String groupId) {
        HashMap<String, List<Membership>> groupToMembers = groupService.getGroupToMembers();
        List<Membership> memberships = groupToMembers.get(groupId);
        return memberships;
    }

    /**
     * method returns all groups belonging to a single user
     *
     * @param userName
     * @return List of Groups
     */
    public List<Group> getGroupsOfUser(String userName) {
        HashMap<String, List<Membership>> userToMembers = groupService.getUserToMembers();
        List<Membership> memberships = userToMembers.get(userName);
        List<Group> groups = memberships.stream().map(member -> member.getGroup()).collect(Collectors.toList());
        return groups;
    }

    /**
     * method returns all users with a pending membership request to the group
     *
     * @param groupId
     * @return List of Memberships
     */
    public List<Membership> getPendingRequestOfGroup(String groupId) {
        HashMap<String, List<Membership>> groupToMembers = groupService.getGroupToMembers();
        List<Membership> memberships = groupToMembers.get(groupId);
        List<Membership> pendingMemberships = memberships.stream()
                .filter(membership -> membership.getMembershipStatus().equals(MembershipStatus.PENDING))
                .collect(Collectors.toList());
        return pendingMemberships;
    }

    /**
     * returns number of pending requests to a group
     *
     * @param groupId
     * @return number of pending requests
     */
    public long countPendingRequestOfGroup(String groupId) {
        HashMap<String, List<Membership>> groupToMembers = groupService.getGroupToMembers();
        List<Membership> memberships = groupToMembers.get(groupId);
        long pendingMemberships = memberships.stream()
                .filter(membership -> membership.getMembershipStatus().equals(MembershipStatus.PENDING))
                .count();
        return pendingMemberships;
    }

    public List<Membership> getMembershipsOfUser(String userName)   {
        HashMap<String, List<Membership>> userToMembers = groupService.getUserToMembers();
        List<Membership> memberships = userToMembers.get(userName);
        return memberships;
    }

    /**
     * returns List of Groups whose groupName fits the given group name
     *
     * @param groupName
     * @return list of groups
     */
    public List<Group> searchGroupByName(String groupName) {
        HashMap<String, Group> groups = groupService.getGroups();
        List<Group> requestedGroups = new ArrayList<>();
        for (Group group : groups.values()) {
            if (group.getName().toString().contains(groupName)) {
                requestedGroups.add(group);
            }
        }
        return requestedGroups;
    }

    /**
     * returns a list of userNames (as String) whose names fit the given userName
     *
     * @param userName
     * @return list of strings (userNames)
     */
    public List<String> searchUserByName(String userName) {
        HashMap<String, User> users = groupService.getUsers();
        List<String> requestedUsers = new ArrayList<>();
        for (User user : users.values()) {
            if (user.getUsername().toString().contains(userName)) {
                requestedUsers.add(user.getUsername().toString());
            }
        }
        return requestedUsers;
    }

    /**
     * start a GroupCreationEvent and one or several MembershipAssignmentEvents
     * MemberShipAssignmentEvent for the GroupCreator always occurs, but is optional for additional members
     *
     * @param groupDescription
     * @param groupName
     * @param groupCourse
     * @param groupCreator
     * @param groupType
     * @param users
     * @return ValidatioResult that tells whether the GroupCreation and MembershipAssignmentEvent(s) were succesful
     */
    public ValidationResult createGroup(String groupDescription, String groupName, String groupCourse,
                                        String groupCreator, String groupType, List<String> users) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults
                .add(groupService.createGroup(groupDescription, groupName, groupCourse, groupCreator, groupType));
        addMembersToGroup(groupCreator, groupType, users, validationResults);
        ValidationResult validationResult = groupService.collectCheck(validationResults);
        return validationResult;
    }

    private void addMembersToGroup(String groupCreator, String groupType, List<String> users, List<ValidationResult> validationResults) {
        if (groupType.equals("PUBLIC")) {
            addMembersToPublicGroup(groupCreator, groupType, users, validationResults);
        } else {
            addMembersToRestrictedGroup(groupCreator, groupType, users, validationResults);
        }
    }

    private void addMembersToRestrictedGroup(String groupCreator, String groupType, List<String> users, List<ValidationResult> validationResults) {
        validationResults
                .add(groupService.assignMembershipToRestrictedGroup(groupCreator, groupService.getLastCreatedGroup(), "ADMIN"));
        for (String user : users) {
            validationResults
                    .add(groupService.assignMembershipToRestrictedGroup(user, groupService.getLastCreatedGroup(), "VIEWER"));
        }
    }

    private void addMembersToPublicGroup(String groupCreator, String groupType, List<String> users, List<ValidationResult> validationResults) {
        validationResults
                .add(groupService.assignMembershipToPublicGroup(groupCreator, groupService.getLastCreatedGroup(), "ADMIN"));
        for (String user : users) {
            validationResults
                    .add(groupService.assignMembershipToPublicGroup(user, groupService.getLastCreatedGroup(), "VIEWER"));
        }
    }

    /**
     * start a GroupDeletionEvent
     *
     * @param groupId
     * @param userName
     * @return ValidationResult that tells whether the GroupDeletionEvent was successful
     */
    public ValidationResult deleteGroup(String groupId, String userName) {
        ValidationResult validationResult = groupService.deleteGroup(groupId, userName);
        return validationResult;
    }

    /**
     * start GroupPropertyUpdateEvent
     *
     * @param groupId
     * @param updatedBy
     * @param groupName
     * @param description
     * @param groupType
     * @return ValidationResult that tells whether the PropertyUpdateEvent was successful
     */
    public ValidationResult updateGroupProperties(String groupId, String updatedBy, String groupName,
                                                  String description, String groupType) {
        ValidationResult validationResult =
                groupService.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);
        return validationResult;
    }

    /**
     * Start a MembershipDeletionEvent
     *
     * @param userName
     * @param groupId
     * @param deletedBy
     * @return ValidationResult that tells whether the user's membership in the given group was successfully deleted
     */
    public ValidationResult deleteMember(String userName, String groupId, String deletedBy) {
        ValidationResult validationResult = groupService.deleteMembership(userName, groupId, deletedBy);
        return validationResult;
    }

    /**
     * Start a MembershipResignmentEvent
     *
     * @param userName
     * @param groupId
     * @return ValidationResult that tells whether the user successfully resigned their membership of the given group
     */
    public ValidationResult resignMembership(String userName, String groupId) {
        ValidationResult validationResult = groupService.resignMembership(userName, groupId);
        return validationResult;
    }

    /**
     * start a MembershipAcceptanceEvent
     *
     * @param userName
     * @param groupId
     * @param acceptedBy
     * @return ValidationResult that tells whether the user's application for the given group was successfully accepted
     */
    public ValidationResult acceptMembership(String userName, String groupId, String acceptedBy) {
        ValidationResult validationResult = groupService.acceptMembership(userName, groupId, acceptedBy);
        return validationResult;
    }

    /**
     * this method starts a MembershipAssignmentEvent if the given group is public and a MembershipRequestEvent if the group is restricted
     * assumes that a user who joins a group does so as a viewer and never directly as an admin
     *
     * @param userName
     * @param groupId
     * @return ValidationResult that tells whether the user successfully joined or applied for the given group
     */
    public ValidationResult joinGroup(String userName, String groupId) {
        ValidationResult validationResult;
        HashMap<String, Group> groups = groupService.getGroups();
        Group group = groups.get(groupId);
        if (group.getGroupType().equals(GroupType.PUBLIC)) {
            validationResult = groupService.assignMembershipToPublicGroup(userName, groupId, "VIEWER");
        } else {
            validationResult = groupService.requestMembership(userName, groupId, "VIEWER");
        }
        return validationResult;
    }

    /**
     * start a MembershipRejectionEvent
     *
     * @param userName
     * @param groupId
     * @param deletedBy
     * @return ValidationResult that tells whether the user's application for the given group was successfully rejected
     */
    public ValidationResult rejectMembership(String userName, String groupId, String deletedBy) {
        ValidationResult validationResult = groupService.rejectMembership(userName, groupId, deletedBy);
        return validationResult;
    }

    /**
     * start an UpdateMembershipEvent
     *
     * @param userName
     * @param groupId
     * @param updatedBy
     * @param updatedTo
     * @return ValidationResult that tells whether the user's membership in the given group was updated successfully
     */
    public ValidationResult updateMembership(String userName, String groupId, String updatedBy, String updatedTo) {
        ValidationResult validationResult = groupService.updateMembership(userName, groupId, updatedBy, updatedTo);
        return validationResult;
    }

    /**
     * start a UserCreationEvent
     *
     * @param userName
     * @return ValidationResult that tells whether the user was created successfully
     */
    public ValidationResult createUser(String userName) {
        ValidationResult validationResult = groupService.createUser(userName);
        return validationResult;
    }


    //TODO alle Veranstaltungen als Liste (noch nicht möglich)
}
