package mops.gruppen1.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Representing the model of a group.
 */
@Getter
@EqualsAndHashCode
public class Group {
    List<Membership> members;
    UUID groupId;
    GroupName name;
    GroupDescription description;
    User groupCreator;
    GroupStatus groupStatus;
    List<Appointment> appointments;

    public Group (List<Membership> members, GroupName name, GroupDescription groupDescription, User groupCreator, GroupStatus groupStatus) {
       this.members = members;
       this.groupId  = UUID.randomUUID();
       this.name = name;
       this.description = groupDescription;
       this.groupCreator = groupCreator;
       this.groupStatus = groupStatus;
       this.appointments = new ArrayList<>();
    }
    public void addMember(Membership membership) {
        members.add(membership);
    }

    public void addAppointment(Appointment appointment){
        appointments.add(appointment);
    }
}
