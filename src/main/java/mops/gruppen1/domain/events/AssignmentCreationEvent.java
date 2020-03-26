package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;

/**
 * TODO: Add check if assignment link has already been assigned and assignment empty to group service.
 */
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@AllArgsConstructor
public class AssignmentCreationEvent implements IEvent {
    private String groupId;
    private String assignmentLink;
    private String createdBy;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Assignment assignment = new Assignment(assignmentLink);
        group.setAssignment(assignment);
    }
}

