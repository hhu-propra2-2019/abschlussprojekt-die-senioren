package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSetup {
    HashMap<Group, List<Membership>> groupToMembers;
    HashMap<User, List<Membership>> userToMembers;
    HashMap<String, Group> groups;
    HashMap<String, User> users = new HashMap<String, User>();
    Group groupOne;
    Group groupTwo;
    List<Membership> memberships = new ArrayList<Membership>();

    public TestSetup() {
        createUserMap();
        createGroups();
        createGroupMap();
        createMemberships();
        addMembersToGroups();
    }

    private void addMembersToGroups() {
        List<Membership> members = groupOne.getMembers();
    }

    private void createGroups() {
        GroupName groupNameOne = new GroupName("Senioren");
        GroupName groupNameTwo = new GroupName("IT-Bois");
        GroupDescription groupDescriptionOne = new GroupDescription("Fancy");
        GroupDescription groupDescriptionTwo = new GroupDescription("Less Fancy");
        GroupStatus groupStatusOne = GroupStatus.ACTIVE;
        GroupStatus groupStatusTwo = GroupStatus.DEACTIVATED;

        User groupCreatorOne = users.get("Max");
        User groupCreatorTwo = users.get("Neo");

        ArrayList<Membership> membershipsOne = new ArrayList<Membership>();
        ArrayList<Membership> membershipsTwo = new ArrayList<Membership>();

        this.groupOne = new Group(membershipsOne, groupNameOne, groupDescriptionOne, groupCreatorOne, groupStatusOne);
        this.groupTwo = new Group(membershipsTwo, groupNameTwo, groupDescriptionTwo, groupCreatorTwo, groupStatusTwo);
    }

    private void createMemberships() {
        Membership memberOne = new Membership(users.get("Max"), groupOne, Type.ADMIN, Status.ACTIVE);
        Membership memberTwo = new Membership(users.get("Stela"), groupOne, Type.VIEWER, Status.ACTIVE);
        Membership memberThree = new Membership(users.get("Neo"), groupTwo, Type.ADMIN, Status.ACTIVE);
        Membership memberFour = new Membership(users.get("Steve"), groupTwo, Type.VIEWER, Status.ACTIVE);

        memberships.add(memberOne);
        memberships.add(memberTwo);
        memberships.add(memberThree);
        memberships.add(memberFour);
    }

    private void createUserMap() {
        User userOne = new User(new Username("Max"));
        User userTwo = new User(new Username("Stela"));
        User userThree = new User(new Username("Neo"));
        User userFour = new User(new Username("Steve"));

        users.put("Max", userOne);
        users.put("Stela", userTwo);
        users.put("Neo", userThree);
        users.put("Steve", userFour);
    }

    private void createGroupMap() {


        groups.put(groupOne.getGroupId().toString(), groupOne);
        groups.put(groupTwo.getGroupId().toString(), groupTwo);
    }
}
