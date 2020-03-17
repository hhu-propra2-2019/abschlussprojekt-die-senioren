package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Appointment;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Add a link of the external AppointmentService to the Group
 */
public class AppointmentCreationEvent implements IEvent {
    private String groupId;
    private String appointmentLink;
    private String createdBy;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Appointment appointment = new Appointment(appointmentLink);
        group.addAppointment(appointment);
    }
}
