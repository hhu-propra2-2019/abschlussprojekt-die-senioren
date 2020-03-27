package mops.gruppen1.applicationService;

import mops.gruppen1.domain.*;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;

@Component
public class CheckService {

    public ValidationResult doesUserExist(String username, HashMap<String, User> users) {
        ValidationResult validationResult = new ValidationResult();
        boolean userDoesNotExist = !users.containsKey(username);
        if (userDoesNotExist) {
            return validationResult;
        }
        validationResult.addError("Nutzer existiert bereits.");
        return validationResult;
    }

    public ValidationResult isGroupActive(String groupId, HashMap<String, Group> groups) {
        ValidationResult validationResult = new ValidationResult();
        Group group = groups.get(groupId);
        boolean isActive = group.getGroupStatus().equals(GroupStatus.ACTIVE);
        if (isActive) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe ist nicht aktiv.");
        return validationResult;
    }

    public ValidationResult doesGroupExist(String groupId, HashMap<String, Group> groups) {
        ValidationResult validationResult = new ValidationResult();
        Group group = groups.get(groupId);
        boolean doesExist = group != null;
        if (doesExist) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe existiert nicht.");
        return validationResult;
    }

    public ValidationResult isPublic(String groupId, HashMap<String, Group> groups) {
        ValidationResult validationResult = new ValidationResult();
        Group group = groups.get(groupId);
        boolean isPublic = group.getGroupType().equals(GroupType.PUBLIC);
        if (isPublic) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe ist nicht öffentlich.");
        return validationResult;
    }

    public ValidationResult isRestricted(String groupId, HashMap<String, Group> groups) {
        ValidationResult validationResult = new ValidationResult();
        Group group = groups.get(groupId);
        boolean isRestricted = group.getGroupType().equals(GroupType.RESTRICTED);
        if (isRestricted) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe ist nicht zugangsbeschränkt.");
        return validationResult;
    }

    public ValidationResult isAdmin(String userName, String groupId, HashMap<String, Group> groups,
                                    HashMap<String, User> users, HashMap<String, List<Membership>> userToMembers) {
        ValidationResult validationResult = new ValidationResult();
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

    public ValidationResult isMembershipActive(String userName, String groupId, HashMap<String, Group> groups,
                                               HashMap<String, User> users, HashMap<String, List<Membership>> userToMembers) {
        ValidationResult validationResult = new ValidationResult();
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

    public ValidationResult isMembershipPending(String userName, String groupId, HashMap<String, Group> groups,
                                                HashMap<String, User> users, HashMap<String, List<Membership>> userToMembers) {
        ValidationResult validationResult = new ValidationResult();
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

    public ValidationResult isMember(String userName, String groupId, HashMap<String, Group> groups,
                                     HashMap<String, User> users, HashMap<String, List<Membership>> userToMembers) {
        ValidationResult validationResult = new ValidationResult();
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        boolean isMember = getMembership(memberships, group) != null;
        if (isMember) {
            return validationResult;
        }
        validationResult.addError("Der Nutzer ist nicht Mitglied der Gruppe.");
        return validationResult;
    }

    public ValidationResult isNotMember(String userName, String groupId, HashMap<String, Group> groups,
                                        HashMap<String, User> users, HashMap<String, List<Membership>> userToMembers) {
        ValidationResult validationResult = new ValidationResult();
        List<Membership> memberships = userToMembers.get(userName);
        Group group = groups.get(groupId);

        boolean isNotMember = getMembership(memberships, group) == null;
        if (isNotMember) {
            return validationResult;
        }
        validationResult.addError("Der Nutzer ist bereits Mitglied der Gruppe.");
        return validationResult;
    }

    public ValidationResult activeAdminRemains(String modifiedBy, String modifiedUser, String groupId,
                                               HashMap<String, List<Membership>> groupToMembers) {
        ValidationResult validationResult = new ValidationResult();
        List<Membership> memberships = groupToMembers.get(groupId);
        Membership membership = memberships.stream().filter(m -> m.getUser().getUsername().toString().equals(modifiedBy)).findFirst().orElse(null);
        boolean isAdmin = membership.getMembershipType().equals(MembershipType.ADMIN);
        if (isAdmin && modifiedBy.equals(modifiedUser)) {

            long adminCount = memberships.stream().filter(m -> m.getMembershipType().equals(MembershipType.ADMIN)).count();
            if (adminCount < 2) {
                validationResult.addError("Es muss zuerst ein anderer Administrator bestimmt werden.");
            }
        }
        return validationResult;
    }

    private Membership getMembership(List<Membership> memberships, Group group) {
        Membership membership = null;

        if(memberships == null) return null;

        for (Membership m : memberships) {
            if (m.getGroup().equals(group)) {
                membership = m;
                break;
            }
        }
        return membership;
    }
}
