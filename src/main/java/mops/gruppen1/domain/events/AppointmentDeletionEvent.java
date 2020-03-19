package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import mops.gruppen1.domain.Appointment;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Delete link of the external AppointmentService in the Group
 * or change representation
 */
@AllArgsConstructor
public class AppointmentDeletionEvent implements IEvent {
    private String groupId;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        group.setAppointment(null);
    }
}
