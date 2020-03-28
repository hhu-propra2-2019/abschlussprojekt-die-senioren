package mops.gruppen1.applicationService;

import mops.gruppen1.data.IEventIdOnly;
import mops.gruppen1.data.IEventRepo;
import mops.gruppen1.data.IGroupIdOnly;
import mops.gruppen1.data.daos.UpdatedGroupsDAO;
import mops.gruppen1.domain.Module;
import mops.gruppen1.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RestServiceTest {

    final Long oldEventId = 2L;
    final Long outdatedOldEventId = 0L;
    RestService restService;

    @BeforeEach
    public void setUp() {

        GroupService groupServiceMock = mock(GroupService.class);
        IEventRepo eventRepoMock = mock(IEventRepo.class);
        this.restService = new RestService(groupServiceMock, eventRepoMock);

        // mock findTopByOrderByIdDesc - return 2L in eventIdList
        List<IEventIdOnly> latestEventIdAsListMock = new ArrayList<>();
        IEventIdOnly eventId = () -> 2L;
        latestEventIdAsListMock.add(eventId);
        when(restService.eventRepo.findTopByOrderByIdDesc()).thenReturn(latestEventIdAsListMock);

        // mock findAllByIdAfter - return list with groupOneId
        List<IGroupIdOnly> groupsChangedMock = new ArrayList<>();
        IGroupIdOnly groupOneId = () -> "groupOneId";
        groupsChangedMock.add(groupOneId);
        when(restService.eventRepo.findDistinctByIdAfter(any())).thenReturn(groupsChangedMock);

        // create group & mock getting group
        List<Membership> members = new ArrayList<>();
        GroupName name = new GroupName("groupName");
        GroupDescription groupDescription = new GroupDescription("description");
        User groupCreator = new User(new Username("groupCreator"));
        GroupStatus groupStatus = GroupStatus.ACTIVE;
        GroupType groupType = GroupType.PUBLIC;
        Module module = new Module();
        module.setModulename(new Modulename("ino1"));
        Group groupOne = new Group(members, name, groupDescription, groupCreator, groupStatus, groupType, module);
        HashMap<String, Group> groupsMocked = new HashMap<>();
        groupsMocked.put("groupOneId", groupOne);

        when(restService.groupService.getGroups()).thenReturn(groupsMocked);
    }

    @Tag("RestServiceTest")
    @Test
    void testGetUpdatedGroupsWithNoUpdatedGroups() {
        //Arrange
        UpdatedGroupsDAO updatedGroupsDAO;

        //Act
        updatedGroupsDAO = restService.getUpdatedGroups(oldEventId);

        //Assert
        assertThat(updatedGroupsDAO.getEventId()).isEqualTo(oldEventId);
    }

    @Tag("RestServiceTest")
    @Test
    void testGetUpdatedGroupsWithUpdatedGroups() {
        //Arrange
        UpdatedGroupsDAO updatedGroupsDAO;

        //Act
        updatedGroupsDAO = restService.getUpdatedGroups(outdatedOldEventId);

        //Assert
        assertThat(updatedGroupsDAO.getEventId()).isNotEqualTo(outdatedOldEventId);
        assertThat(updatedGroupsDAO.getEventId()).isEqualTo(2L);
        assertThat(updatedGroupsDAO.getGroupDAOs().get(0).getGroupId()).isEqualTo("groupOneId");
    }
}
