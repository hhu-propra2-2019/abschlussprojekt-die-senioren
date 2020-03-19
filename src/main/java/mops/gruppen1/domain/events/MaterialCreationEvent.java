package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.HashMap;
import java.util.List;


/**
 * Add a link of the external MaterialService to the Group
 * @all: TODO: Add check for already existing Material link to Group Service
 */
@AllArgsConstructor
public class MaterialCreationEvent implements IEvent {
    private String groupId;
    private String materialLink;
    private String createdBy;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Material material = new Material(materialLink);
     //   group.setAppointment(appointment);
    }
}
