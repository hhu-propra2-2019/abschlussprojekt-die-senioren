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

    public ValidationResult deleteGroup(String groupId, String userName) {
        ValidationResult validationResult = isAdmin(userName, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        if (validationResult.isValid()) {
            performGroupDeletionEvent(userName, groupId);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult updateGroupProperties(String groupId, String updatedBy, String groupName, String description, String groupType) {
        ValidationResult validationResult = isAdmin(updatedBy, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        if (validationResult.isValid()) {
            performGroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult addAppointment(String groupId, String createdBy, String link) {
        ValidationResult validationResult = isAdmin(createdBy, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = hasNoAppointment(groupId, validationResult);
        if (validationResult.isValid()) {
            performAppointmentCreationEvent(groupId, createdBy, link);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult deleteAppointment(String groupId, String deletedBy, String link) {
        ValidationResult validationResult = isAdmin(deletedBy, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = hasAppointment(groupId, validationResult);
        if (validationResult.isValid()) {
            performAppointmentDeletionEvent(groupId, deletedBy);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult createForum(String groupId, String createdBy, String link) {
        ValidationResult validationResult = isAdmin(createdBy, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = hasNoForum(groupId, validationResult);
        if (validationResult.isValid()) {
            performForumCreationEvent(groupId, createdBy, link);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult createAssignment(String groupId, String createdBy, String link) {
        ValidationResult validationResult = isGroupActive(groupId, new ValidationResult());
        validationResult = hasNoAssignment(groupId, validationResult);
        if (validationResult.isValid()) {
            performAssignmentCreationEvent(groupId, createdBy, link);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult createMaterial(String groupId, String createdBy, String link) {
        ValidationResult validationResult = isAdmin(createdBy, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = hasNoMaterial(groupId, validationResult);
        if (validationResult.isValid()) {
            performMaterialCreationEvent(groupId, createdBy, link);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult deleteMaterial(String groupId, String createdBy) {
        ValidationResult validationResult = isAdmin(createdBy, groupId, new ValidationResult());
        validationResult = isGroupActive(groupId, validationResult);
        validationResult = hasMaterial(groupId, validationResult);
        if (validationResult.isValid()) {
            performMaterialDeletionEvent(groupId, createdBy);
            return validationResult;
        }
        return validationResult;
    }

    public ValidationResult createUser(String userName) {
        ValidationResult validationResult = new ValidationResult();
        performUserCreationEvent(userName);
        return validationResult;
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

    private ValidationResult hasNoAssignment(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean hasNoAssignment = group.getAssignment() == null;
        if (hasNoAssignment) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe hat bereits eine bereits bestehende Abgabe.");
        return validationResult;
    }


    private ValidationResult hasNoForum(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean hasNoForum = group.getForum() == null;
        if (hasNoForum) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe hat bereits ein Forum.");
        return validationResult;
    }

    private ValidationResult hasMaterial(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean hasMaterial = group.getMaterial() != null;
        if (hasMaterial) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe hat keine Materialsammlung.");
        return validationResult;
    }

    private ValidationResult hasNoMaterial(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean hasNoMaterial = group.getMaterial() == null;
        if (hasNoMaterial) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe hat bereits eine Materialsammlung.");
        return validationResult;
    }

    private ValidationResult hasAppointment(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean hasAppointment = group.getAppointment() != null;
        if (hasAppointment) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe hat kein Appointment.");
        return validationResult;
    }

    private ValidationResult hasNoAppointment(String groupId, ValidationResult validationResult) {
        Group group = groups.get(groupId);
        boolean hasNoAppointment = group.getAppointment() == null;
        if (hasNoAppointment) {
            return validationResult;
        }
        validationResult.addError("Die Gruppe hat bereits ein Appointment.");
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

    public void performGroupDeletionEvent(String userName, String groupId) {
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, userName);
        groupDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupDeletionEventDTO = events.createEventDTO(userName, groupId, timestamp, "GroupDeletionEvent", groupDeletionEvent);

        events.saveToRepository(groupDeletionEventDTO);

    }

    private void performGroupPropertyUpdateEvent(String groupId, String updatedBy, String groupName, String description, String groupType) {
        GroupPropertyUpdateEvent groupPropertyUpdateEvent = new GroupPropertyUpdateEvent(groupId, updatedBy, groupName, description, groupType);
        groupPropertyUpdateEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupPropertyUpdateEventDTO = events.createEventDTO(updatedBy, groupId, timestamp, "GroupPropertyUpdateEvent", groupPropertyUpdateEvent);

        events.saveToRepository(groupPropertyUpdateEventDTO);
    }

    private void performAppointmentCreationEvent(String groupId, String createdBy, String link) {
        AppointmentCreationEvent appointmentCreationEvent = new AppointmentCreationEvent(groupId, link, createdBy);
        appointmentCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO appointmentCreationEventDTO = events.createEventDTO(createdBy, groupId, timestamp, "AppointmentCreationEvent", appointmentCreationEvent);

        events.saveToRepository(appointmentCreationEventDTO);
    }

    private void performAppointmentDeletionEvent(String groupId, String deletedBy) {
        AppointmentDeletionEvent appointmentDeletionEvent = new AppointmentDeletionEvent(groupId);
        appointmentDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO appointmentDeletionEventDTO = events.createEventDTO(deletedBy, groupId, timestamp, "AppointmentDeletionEvent", appointmentDeletionEvent);

        events.saveToRepository(appointmentDeletionEventDTO);
    }

    private void performForumCreationEvent(String groupId, String createdBy, String link) {
        ForumCreationEvent forumCreationEvent = new ForumCreationEvent(groupId, link, createdBy);
        forumCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO forumCreationEventDTO = events.createEventDTO(createdBy, groupId, timestamp, "ForumCreationEvent", forumCreationEvent);

        events.saveToRepository(forumCreationEventDTO);
    }

    private void performAssignmentCreationEvent(String groupId, String createdBy, String link) {
        AssignmentCreationEvent assignmentCreationEvent = new AssignmentCreationEvent(groupId, link, createdBy);
        assignmentCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO assignmentCreationEventDTO = events.createEventDTO(createdBy, groupId, timestamp, "AssignmentCreationEvent", assignmentCreationEvent);

        events.saveToRepository(assignmentCreationEventDTO);
    }

    private void performMaterialCreationEvent(String groupId, String createdBy, String link) {
        MaterialCreationEvent materialCreationEvent = new MaterialCreationEvent(groupId, link, createdBy);
        materialCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO materialCreationEventDTO = events.createEventDTO(createdBy, groupId, timestamp, "MaterialCreationEvent", materialCreationEvent);

        events.saveToRepository(materialCreationEventDTO);
    }

    private void performMaterialDeletionEvent(String groupId, String createdBy) {
        MaterialDeletionEvent materialDeletionEvent = new MaterialDeletionEvent(groupId);
        materialDeletionEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO materialDeletionEventDTO = events.createEventDTO(createdBy, groupId, timestamp, "MaterialDeletionEvent", materialDeletionEvent);

        events.saveToRepository(materialDeletionEventDTO);
    }

    private void performUserCreationEvent(String userName) {
        UserCreationEvent userCreationEvent = new UserCreationEvent(userName);
        userCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO userCreationEventDTO = events.createEventDTO(null, null, timestamp, "UserCreationEvent", userCreationEvent);

        events.saveToRepository(userCreationEventDTO);
    }

}
