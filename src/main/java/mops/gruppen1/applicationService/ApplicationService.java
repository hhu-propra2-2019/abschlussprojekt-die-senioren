package mops.gruppen1.applicationService;

import mops.gruppen1.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that controles calls to GroupService
 */
@Service
public class ApplicationService {
    @Autowired
    GroupService groupService;

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

    public long countPendingRequestOfGroup(String groupId) {
        HashMap<String, List<Membership>> groupToMembers = groupService.getGroupToMembers();
        List<Membership> memberships = groupToMembers.get(groupId);
        long pendingMemberships = memberships.stream()
                .filter(membership -> membership.getMembershipStatus().equals(MembershipStatus.PENDING))
                .count();
        return pendingMemberships;
    }

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

    public ValidationResult createGroup(String groupDescription, String groupName, String groupCourse,
                                        String groupCreator, String groupType, List<String> users) {
        List<ValidationResult> validationResults = new ArrayList<>();
        validationResults
                .add(groupService.createGroup(groupDescription, groupName, groupCourse, groupCreator, groupType));
        validationResults
                .add(groupService.assignMembership(groupCreator, groupService.getLastCreatedGroup(), "ADMIN"));
        for (String user : users) {
            validationResults
                    .add(groupService.assignMembership(user, groupService.getLastCreatedGroup(), "VIEWER"));
        }
        ValidationResult validationResult = groupService.collectCheck(validationResults);
        return validationResult;
    }

    public ValidationResult deleteGroup(String groupId, String userName) {
        ValidationResult validationResult = groupService.deleteGroup(groupId, userName);
        return validationResult;
    }

    public ValidationResult updateGroupProperties(String groupId, String updatedBy, String groupName,
                                                  String description, String groupType) {
        ValidationResult validationResult =
                groupService.updateGroupProperties(groupId, updatedBy, groupName, description, groupType);
        return validationResult;
    }

    public ValidationResult deleteMember(String userName, String groupId, String deletedBy) {
        ValidationResult validationResult = groupService.deleteMembership(userName, groupId, deletedBy);
        return validationResult;
    }

    public ValidationResult resignMembership(String userName, String groupId) {
        ValidationResult validationResult = groupService.resignMembership(userName, groupId);
        return validationResult;
    }

    public ValidationResult acceptMembership(String userName, String groupId, String acceptedBy) {
        ValidationResult validationResult = groupService.acceptMembership(userName, groupId, acceptedBy);
        return validationResult;
    }

    public ValidationResult joinGroup(String userName, String groupId) {
        ValidationResult validationResult;
        HashMap<String, Group> groups = groupService.getGroups();
        Group group = groups.get(groupId);
        if (group.getGroupType().equals(GroupType.PUBLIC)) {
            validationResult = groupService.assignMembership(userName, groupId, "VIEWER");
        } else {
            validationResult = groupService.requestMembership(userName, groupId, "VIEWER");
        }
        return validationResult;
    }

    public ValidationResult rejectMembership(String userName, String groupId) {
        ValidationResult validationResult = groupService.rejectMembership(userName, groupId);
        return validationResult;
    }

    public ValidationResult updateMembership(String userName, String groupId, String updatedBy, String updatedTo) {
        ValidationResult validationResult = groupService.updateMembership(userName, groupId, updatedBy, updatedTo);
        return validationResult;
    }

    public ValidationResult createUser(String userName) {
        ValidationResult validationResult = groupService.createUser(userName);
        return validationResult;
    }


    //TODO alle Veranstaltungen als Liste (noch nicht möglich)
}
