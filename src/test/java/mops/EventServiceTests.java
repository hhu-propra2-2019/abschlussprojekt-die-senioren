package mops;

import mops.gruppen1.applicationService.EventService;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.GroupType;
import mops.gruppen1.domain.events.IEvent;
import mops.gruppen1.domain.events.TestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;


@SpringBootTest(classes = GruppenbildungApplicationTests.class)
class EventServiceTests {

    EventService eventService;

    @BeforeEach
    public void setUp() {
        EventRepo eventRepoMock = mock(EventRepo.class);
        this.eventService = new EventService(eventRepoMock);
    }

    @Test
    void testTransform() {

        //arrange
        String testUserName = "test_user";
        String testGroupName = "test_group";
        String testEventType = "TestEvent";
        LocalDateTime timestamp = LocalDateTime.parse("2020-03-13T10:01:33");
        Long testGroupID = 99L;
        String testGroupDescription = "This is a group description.";
        GroupType groupType= GroupType.PUBLIC;
        Long groupCourse = 2L;
        String testPayload = "{" +
                "\"eventId\": \"6\"," +
                "\"timestamp\": \"2020-03-02T13:22:14\"," +
                "\"testEventType\": \"TestEvent\"," +
                "\"eventParameters\": {" +
                "\"testGroupId\": \"1245465\"," +
                "\"testGroupName\": \"Gruppe1\"," +
                "\"testGroupCreation\": \"2020-03-13T10:01:33\"," +
                "\"groupCreator\": \"user1\"," +
                "\"groupDescription\": \"This is a group description.\"," +
                "\"groupType\": \"PUBLIC\"," +
                "\"groupCourse\": \"2\"" +
                "}"+
                "}";

        EventDTO testEventDTO = new EventDTO(testUserName, testGroupName,timestamp, testEventType, testPayload);

        //act
        IEvent testEvent = eventService.transform(testEventDTO);

        //assert
        assertThat(testEvent, isA(TestEvent.class));
    }
}
