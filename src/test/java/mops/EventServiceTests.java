package mops;

import mops.gruppen1.applicationService.EventService;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.events.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;


@SpringBootTest(classes = GruppenbildungApplicationTests.class)
class EventServiceTests {

    static EventService eventService;

    @BeforeAll
    static void InitEventRepoMock() {
        eventService = new EventService();
    }

    @Test
    void testTransform() {
        String testUserName = "test_user";
        String testGroupName = "test_group";
        String testEventType = "GroupCreationEvent";
        String testPayload = "test_payload";

        EventDTO testEventDTO = new EventDTO(testUserName, testGroupName, testEventType, testPayload);

        Event testEvent = eventService.transform(testEventDTO);


    }

}
