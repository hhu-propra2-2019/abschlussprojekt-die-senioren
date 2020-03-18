package mops.gruppen1.domain.events;

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
public class AppointmentDeletionEvent implements IEvent {
    private String groupId;
    private String appointmentLink;
    private String deletedBy;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        group.setAppointment(null);
    }
}
