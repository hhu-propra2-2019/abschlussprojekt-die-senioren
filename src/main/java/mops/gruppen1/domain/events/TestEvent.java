package mops.gruppen1.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import java.util.HashMap;
import java.util.List;

/**
 * Fake event, which is used for testing the transformation of DTO´s to a specific eventType and vice versa.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class TestEvent implements IEvent {

    //Database and Payload fields
    private String userName;
    private String groupID;
    private String eventType;

    //Payload fields
    private String groupDescription;
    private String groupType;

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
    }
}
