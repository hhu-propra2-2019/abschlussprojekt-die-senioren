package mops.gruppen1.applicationService;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.MembershipStatus;
import mops.gruppen1.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApplicationService {
    @Autowired
    public GroupService groupService;

    public List<Membership> getMembersOfGroup(String groupId) {
        HashMap<String, List<Membership>> groupToMembers = groupService.getGroupToMembers();
        List<Membership> memberships = groupToMembers.get(groupId);
        return memberships;
    }

    public List<Group> getGroupsOfUser(String userName) {
        HashMap<String, List<Membership>> userToMembers = groupService.getUserToMembers();
        List<Membership> memberships = userToMembers.get(userName);
        List<Group> groups = memberships.stream().map(member -> member.getGroup()).collect(Collectors.toList());
        return groups;
    }

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

    public List<Membership> getMembershipsOfUser(String userName)   {
        HashMap<String, List<Membership>> userToMembers = groupService.getUserToMembers();
        List<Membership> memberships = userToMembers.get(userName);
        return memberships;
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

    // alle Veranstaltungen als Liste (noch nicht möglich)

    // alle Events
    
    // neuer Check für isNotTheOnlyAdmin oder doesGroupStillHaveAnAdmin
}
