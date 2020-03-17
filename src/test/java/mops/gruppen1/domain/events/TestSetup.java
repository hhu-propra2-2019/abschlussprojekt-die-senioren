package mops.gruppen1.domain.events;

import mops.gruppen1.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TestSetup {
    HashMap<Group, List<Membership>> groupToMembers = new HashMap<Group, List<Membership>>();
    HashMap<User, List<Membership>> userToMembers = new HashMap<User, List<Membership>>();
    HashMap<String, Group> groups = new HashMap<String, Group>();
    HashMap<String, User> users = new HashMap<String, User>();
    private Group groupOne;
    private Group groupTwo;
    private List<Membership> memberships = new ArrayList<Membership>();

    public TestSetup() {
        createUserMap();
        createGroups();
        createGroupMap();
        createMemberships();
        addMembersToGroups();
        createGroupToMembershipHashMap();
        createUserToMembershipHashMap();
    }

    private void createUserToMembershipHashMap() {
        ArrayList<Membership> listMax = new ArrayList<Membership>();
        listMax.add(memberships.get(0));
        ArrayList<Membership> listStela = new ArrayList<Membership>();
        listStela.add(memberships.get(1));
        ArrayList<Membership> listNeo = new ArrayList<Membership>();
        listNeo.add(memberships.get(2));
        ArrayList<Membership> listSteve = new ArrayList<Membership>();
        listSteve.add(memberships.get(3));

        userToMembers.put(users.get("Max"), listMax);
        userToMembers.put(users.get("Stela"), listStela);
        userToMembers.put(users.get("Neo"), listNeo);
        userToMembers.put(users.get("Steve"), listSteve);
    }

    private void createGroupToMembershipHashMap() {
        List<Membership> membersOne = groupOne.getMembers();
        List<Membership> membersTwo = groupTwo.getMembers();
        groupToMembers.put(groupOne, membersOne);
        groupToMembers.put(groupTwo, membersTwo);
    }

    private void addMembersToGroups() {
        groupOne.addMember(memberships.get(0));
        groupOne.addMember(memberships.get(1));
        groupTwo.addMember(memberships.get(2));
        groupTwo.addMember(memberships.get(3));
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
