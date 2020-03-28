package mops.gruppen1.domain.events;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Module;
import mops.gruppen1.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * create new Group with attributes groupId, name, description and, if added, GroupStatus (to 'active').
 * Add Group to HashSet Groups
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class GroupCreationEvent implements IEvent {

    private String groupId;
    private String groupDescription;
    private String groupName;
    private String groupCourse;
    private String groupCreator;
    private String groupType;

    public GroupCreationEvent(String groupDescription, String groupName, String groupCourse,
                              String groupCreator, String groupType) {
        this.groupDescription = groupDescription;
        this.groupName = groupName;
        this.groupCourse = groupCourse;
        this.groupCreator = groupCreator;
        this.groupType = groupType;
    }

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers,
                        HashMap<String, List<Membership>> userToMembers,
                        HashMap<String, User> users, HashMap<String, Group> groups) {
        Group newGroup = createGroup();
        this.groupId = newGroup.getGroupId().toString();
        groups.put(groupId, newGroup);
        groupToMembers.put(this.groupId, new ArrayList<>());
    }

    private Group createGroup() {
        List<Membership> members = new ArrayList<>();
        GroupName name = new GroupName(this.groupName);
        GroupDescription description = new GroupDescription(this.groupDescription);
        GroupStatus groupStatus = GroupStatus.ACTIVE;
        User groupCreator = new User(new Username(this.groupCreator));
        GroupType groupType = GroupType.valueOf(this.groupType.toUpperCase());
        Module module = new Module();
        module.setModulename(new Modulename(groupCourse));

        return createDependingOnArgs(members, name, description, groupStatus, groupCreator, groupType, module);
    }

    private Group createDependingOnArgs(List<Membership> members, GroupName name,
                                        GroupDescription description, GroupStatus groupStatus,
                                        User groupCreator, GroupType groupType, Module module) {
        if (groupId == null) {
            return new Group(members, name, description, groupCreator, groupStatus, groupType, module);
        }
        return new Group(members, UUID.fromString(groupId), name, description,
                groupCreator, groupStatus, groupType, module);
    }
}
