package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Module;
import mops.gruppen1.domain.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * class provides testing environment
 * 3 groups with 2 users each
 * GroupOne is active, GroupTwo is deactivated, GroupThree is active but restricted
 */
public class TestSetup {
    public HashMap<String, List<Membership>> groupToMembers = new HashMap<>();
    public HashMap<String, List<Membership>> userToMembers = new HashMap<>();
    public HashMap<String, Group> groups = new HashMap<>();
    public HashMap<String, User> users = new HashMap<>();
    public Group groupOne;
    public Group groupTwo;
    public Group groupThree;
    public List<Membership> memberships = new ArrayList<>();

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
        ArrayList<Membership> listMax = new ArrayList<>();
        listMax.add(memberships.get(0));
        ArrayList<Membership> listStela = new ArrayList<>();
        listStela.add(memberships.get(1));
        ArrayList<Membership> listNeo = new ArrayList<>();
        listNeo.add(memberships.get(2));
        ArrayList<Membership> listSteve = new ArrayList<>();
        listSteve.add(memberships.get(3));
        ArrayList<Membership> listDiego = new ArrayList<>();
        listDiego.add(memberships.get(4));
        ArrayList<Membership> listMilten = new ArrayList<>();
        listMilten.add(memberships.get(5));


        userToMembers.put(users.get("Max").getUsername().getUsername(), listMax);
        userToMembers.put(users.get("Stela").getUsername().getUsername(), listStela);
        userToMembers.put(users.get("Neo").getUsername().getUsername(), listNeo);
        userToMembers.put(users.get("Steve").getUsername().getUsername(), listSteve);
        userToMembers.put(users.get("Diego").getUsername().getUsername(), listDiego);
        userToMembers.put(users.get("Milten").getUsername().getUsername(), listMilten);
    }

    private void createGroupToMembershipHashMap() {
        List<Membership> membersOne = groupOne.getMembers();
        List<Membership> membersTwo = groupTwo.getMembers();
        List<Membership> membersThree = groupThree.getMembers();
        groupToMembers.put(groupOne.getGroupId().toString(), membersOne);
        groupToMembers.put(groupTwo.getGroupId().toString(), membersTwo);
        groupToMembers.put(groupThree.getGroupId().toString(), membersThree);
    }

    private void addMembersToGroups() {
        groupOne.addMember(memberships.get(0));
        groupOne.addMember(memberships.get(1));
        groupTwo.addMember(memberships.get(2));
        groupTwo.addMember(memberships.get(3));
        groupThree.addMember(memberships.get(4));
        groupThree.addMember(memberships.get(5));
    }

    private void createGroups() {
        GroupName groupNameOne = new GroupName("Senioren");
        GroupName groupNameTwo = new GroupName("IT-Bois");
        GroupName groupNameThree = new GroupName("das alte Lager");
        GroupDescription groupDescriptionOne = new GroupDescription("Fancy");
        GroupDescription groupDescriptionTwo = new GroupDescription("Less Fancy");
        GroupDescription groupDescriptionThree = new GroupDescription("Even less fancy");
        GroupStatus groupStatusOne = GroupStatus.ACTIVE;
        GroupStatus groupStatusTwo = GroupStatus.DEACTIVATED;
        GroupStatus groupStatusThree = GroupStatus.ACTIVE;
        GroupType groupTypeOne = GroupType.PUBLIC;
        GroupType groupTypeTwo = GroupType.RESTRICTED;
        GroupType groupTypeThree = GroupType.RESTRICTED;
        Module moduleOne = new Module();
        moduleOne.setModulename(new ModuleName("Info1"));
        Module moduleTwo = new Module();
        moduleTwo.setModulename(new ModuleName("Keine Veranstaltung."));


        User groupCreatorOne = users.get("Max");
        User groupCreatorTwo = users.get("Neo");
        User groupCreatorThree = users.get("Diego");

        ArrayList<Membership> membershipsOne = new ArrayList<>();
        ArrayList<Membership> membershipsTwo = new ArrayList<>();
        ArrayList<Membership> membershipsThree = new ArrayList<>();

        this.groupOne = new Group(membershipsOne, groupNameOne, groupDescriptionOne, groupCreatorOne, groupStatusOne, groupTypeOne, moduleOne);
        this.groupTwo = new Group(membershipsTwo, groupNameTwo, groupDescriptionTwo, groupCreatorTwo, groupStatusTwo, groupTypeTwo, moduleTwo);
        this.groupThree = new Group(membershipsThree, groupNameThree, groupDescriptionThree, groupCreatorThree, groupStatusThree, groupTypeThree, moduleOne);

    }

    private void createMemberships() {
        Membership memberOne = new Membership(users.get("Max"), groupOne, MembershipType.ADMIN, MembershipStatus.ACTIVE);
        Membership memberTwo = new Membership(users.get("Stela"), groupOne, MembershipType.VIEWER, MembershipStatus.ACTIVE);
        Membership memberThree = new Membership(users.get("Neo"), groupTwo, MembershipType.ADMIN, MembershipStatus.ACTIVE);
        Membership memberFour = new Membership(users.get("Steve"), groupTwo, MembershipType.VIEWER, MembershipStatus.ACTIVE);
        Membership memberFive = new Membership(users.get("Diego"), groupThree, MembershipType.ADMIN, MembershipStatus.ACTIVE);
        Membership memberSix = new Membership(users.get("Milten"), groupThree, MembershipType.VIEWER, MembershipStatus.PENDING);

        memberships.add(memberOne);
        memberships.add(memberTwo);
        memberships.add(memberThree);
        memberships.add(memberFour);
        memberships.add(memberFive);
        memberships.add(memberSix);
    }

    private void createUserMap() {
        User userOne = new User(new Username("Max"));
        User userTwo = new User(new Username("Stela"));
        User userThree = new User(new Username("Neo"));
        User userFour = new User(new Username("Steve"));
        User userFive = new User(new Username("Diego"));
        User userSix = new User(new Username("Milten"));

        users.put("Max", userOne);
        users.put("Stela", userTwo);
        users.put("Neo", userThree);
        users.put("Steve", userFour);
        users.put("Diego", userFive);
        users.put("Milten", userSix);
    }

    private void createGroupMap() {
        groups.put(groupOne.getGroupId().toString(), groupOne);
        groups.put(groupTwo.getGroupId().toString(), groupTwo);
        groups.put(groupThree.getGroupId().toString(), groupThree);
    }
}
