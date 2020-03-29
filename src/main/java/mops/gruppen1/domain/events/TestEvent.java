package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import java.util.HashMap;
import java.util.List;

/**
 * Fake event, which is used for testing the transformation of DTO´s to a specific eventType and vice versa.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TestEvent implements IEvent {

    //Database and Payload fields
    private String user;
    private String group;
    private String eventType;

    //Payload fields
    private String groupDescription;
    private String groupType;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
    }
}
