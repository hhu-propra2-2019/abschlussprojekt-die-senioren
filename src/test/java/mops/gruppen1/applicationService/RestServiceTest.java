package mops.gruppen1.applicationService;

import mops.gruppen1.data.DAOs.CurrentStateDAO;
import mops.gruppen1.data.EventIdOnly;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.data.GroupIdOnly;
import mops.gruppen1.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestServiceTest {
    final Long oldEventId = 2L;
    final Long outdatedOldEventId = 0L;
    RestService restService;

    @BeforeEach
    public void setUp() {

        GroupService groupServiceMock = mock(GroupService.class);
        EventRepo eventRepoMock = mock(EventRepo.class);
        this.restService = new RestService(groupServiceMock, eventRepoMock);

        // mock findTopByOrderByIdDesc - return 2L in eventIdList
        List<EventIdOnly> latestEventIdAsListMock = new ArrayList<>();
        EventIdOnly eventId = () -> 2L;
        latestEventIdAsListMock.add(eventId);
        when(restService.eventRepo.findTopByOrderByIdDesc()).thenReturn(latestEventIdAsListMock);

        // mock findAllByIdAfter - return list with groupOneId
        List<GroupIdOnly> groupsChangedMock = new ArrayList<>();
        GroupIdOnly groupOneId = () -> "groupOneId";
        groupsChangedMock.add(groupOneId);
        when(restService.eventRepo.findAllByIdAfter(oldEventId)).thenReturn(groupsChangedMock);

        // create group & mock getting group
        List<Membership> members = new ArrayList<>();
        GroupName name = new GroupName("groupName");
        GroupDescription groupDescription = new GroupDescription("description");
        User groupCreator = new User(new Username("groupCreator"));
        GroupStatus groupStatus = GroupStatus.ACTIVE;
        GroupType groupType = GroupType.PUBLIC;
        Group groupOne = new Group(members, name, groupDescription, groupCreator, groupStatus, groupType);
        HashMap<String, Group> groupsMocked = new HashMap<>();
        groupsMocked.put("groupOneId", groupOne);

        when(restService.groupService.getGroups()).thenReturn(groupsMocked);
    }

    @Tag("RestServiceTest")
    @Test
    void testGetUpdatedGroupsWithNoUpdatedGroups() {
        //Arrange
        CurrentStateDAO currentStateDAO;

        //Act
        currentStateDAO = restService.getUpdatedGroups(oldEventId);

        //Assert
        assertThat(currentStateDAO.getEventId()).isEqualTo(oldEventId); // Klasse CurrentStateDaos braucht Getter!
    }

    @Tag("RestServiceTest")
    @Test
    void testGetUpdatedGroupsWithUpdatedGroups() {
        //Arrange
        CurrentStateDAO currentStateDAO;

        //Act
        currentStateDAO = restService.getUpdatedGroups(outdatedOldEventId);

        //Assert
        assertThat(currentStateDAO.getEventId()).isNotEqualTo(outdatedOldEventId);
        assertThat(currentStateDAO.getEventId()).isEqualTo(oldEventId);
        assertThat(currentStateDAO.getEventId()).isEqualTo(2L);
        assertThat(currentStateDAO.getGroupDAOs().get(0).getGroupId()).isEqualTo("groupOneId"); // Klasse GroupDAOs braucht Getter!
    }
}
