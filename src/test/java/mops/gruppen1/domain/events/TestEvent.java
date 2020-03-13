package mops.gruppen1.domain.events;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.GroupType;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * Tests if transformation of DTO to specific EventClass is working.
 */

public class TestEvent implements Event {

    private String testUserName = "test_user";
    private String testGroupName = "test_group";
    private String testEventType = "TestEvent";
    private LocalDateTime timestamp = LocalDateTime.parse("2020-03-13T10:01:33");
    private Long testGroupID = 99L;
    private String testGroupDescription = "Lalala";
    private GroupType groupType= GroupType.RESTRICTED;
    private mops.gruppen1.domain.Module groupCourse = null;


    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashSet<User> users, HashSet<Group> groups) {

    }
}
