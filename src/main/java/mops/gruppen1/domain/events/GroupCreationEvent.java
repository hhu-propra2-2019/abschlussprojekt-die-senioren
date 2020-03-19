package mops.gruppen1.domain.events;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create new Group with attributes groupId, name, description and, if added, GroupStatus (to 'active')
 * Add Group to HashSet Groups
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreationEvent implements IEvent {

    private String groupID;
    private String groupDescription;
    private String groupName;
    private String groupCourse;
    private String groupCreator;
    private String groupType;
    //TODO how to handle optional groupCourse?


    public GroupCreationEvent(String groupDescription, String groupName, String groupCourse, String groupCreator, String groupType) {
        this.groupDescription = groupDescription;
        this.groupName = groupName;
        this.groupCourse = groupCourse;
        this.groupCreator = groupCreator;
        this.groupType = groupType;
    }

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group newGroup = createGroup();
        this.groupID = newGroup.getGroupId().toString();
        groups.put(groupID, newGroup);
        groupToMembers.put(newGroup, new ArrayList<>());
    }

    private Group createGroup() {
        List<Membership> members = new ArrayList<>();
        GroupName name = new GroupName(this.groupName);
        GroupDescription description = new GroupDescription(this.groupDescription);
        GroupStatus groupStatus = GroupStatus.ACTIVE;
        User groupCreator = new User(new Username(this.groupCreator));
        GroupType groupType = GroupType.valueOf(this.groupType.toUpperCase());
        //add groupCourse?

        return new Group(members, name, description, groupCreator, groupStatus, groupType);
    }
}
