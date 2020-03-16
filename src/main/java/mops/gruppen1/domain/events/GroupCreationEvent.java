package mops.gruppen1.domain.events;

import lombok.*;
import mops.gruppen1.domain.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * create new Group with attributes groupId, name, description and, if added, GroupStatus (to 'active')
 * Add Group to HashSet Groups
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GroupCreationEvent implements Event {

    private String groupID;
    private String groupDescription;
    private String groupName;
    private String groupCourse;
    private String groupCreator;
    private LocalDateTime timestamp = null;
    //TODO how to handle optional groupCourse?
    //private Long eventId; TODO add


    public GroupCreationEvent(String groupDescription, String groupName, String groupCourse, String groupCreator) {
        this.groupID = UUID.randomUUID().toString();
        this.groupDescription = groupDescription;
        this.groupName = groupName;
        this.groupCourse = groupCourse;
        this.groupCreator = groupCreator;
    }

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group newGroup = createGroup();
        groups.put(groupID, newGroup);
    }

    private Group createGroup() {
        List<Membership> members = new ArrayList<>();
        GroupName name = new GroupName(this.groupName);
        GroupDescription description = new GroupDescription(this.groupDescription);
        UUID id = UUID.fromString(groupID);
        GroupStatus groupStatus = GroupStatus.ACTIVE;
        User groupCreator = new User(new Username(this.groupCreator));
        //add groupCourse?

        return new Group(members, id, name, description, groupCreator, groupStatus);
    }
}
