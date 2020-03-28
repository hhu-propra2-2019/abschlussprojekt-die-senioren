package mops;

import mops.gruppen1.applicationService.EventService;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.IEventRepo;
import mops.gruppen1.domain.events.IEvent;
import mops.gruppen1.domain.events.TestEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.hamcrest.MatcherAssert.assertThat;
import java.time.LocalDateTime;
import static org.hamcrest.Matchers.isA;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;


@SpringBootTest(classes = GruppenbildungApplicationTests.class)
class EventServiceTests {

    EventService eventService;

    @BeforeEach
    public void setUp() {
        IEventRepo eventRepoMock = mock(IEventRepo.class);
        this.eventService = new EventService(eventRepoMock);
    }

    @Test
    void deserialize() {

        //arrange
        String userName = "user1";
        String groupID = "12345";
        String eventType = "TestEvent";
        LocalDateTime timestamp = LocalDateTime.parse("2020-03-02T13:22:14");
        String testPayload = "{"
                + "\"groupDescription\": \"This is a group description.\","
                + "\"groupType\": \"PUBLIC\""
                + "}";

        EventDTO testEventDTO = new EventDTO(userName, groupID, timestamp, eventType, testPayload);

        //act
        IEvent testEvent = eventService.transform(testEventDTO);

        //assert
        assertThat(testEvent, isA(TestEvent.class));
    }

    @Test
    void serialize() {

        //arrange
        TestEvent testEvent = new TestEvent("test_user", "99", "TestEvent", "This is a group description.", "PUBLIC");

        //act
        EventDTO eventDTO = eventService.createEventDTO("test_user", "99", LocalDateTime.parse("2020-03-13T10:01:33"), "TestEvent", testEvent);

        //assert
        assertThat(eventDTO.getUser(), is("test_user"));
        assertThat(eventDTO.getEventType(), is("TestEvent"));
        assertThat(eventDTO.getTimestamp().toString(), is("2020-03-13T10:01:33"));
        assertThat(eventDTO.getGroup(), is("99"));
        assertThat(eventDTO.getPayload(), is("{"
                + "\"user\":\"test_user\","
                + "\"group\":\"99\","
                + "\"eventType\":\"TestEvent\","
                + "\"groupDescription\":\"This is a group description.\","
                + "\"groupType\":\"PUBLIC\""
                + "}"));
    }
}
