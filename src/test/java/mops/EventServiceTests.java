package mops;

import mops.gruppen1.applicationService.EventService;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.events.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.hamcrest.MatcherAssert.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


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
        LocalDateTime timestamp = LocalDateTime.now();
        String testEventType = "GroupCreationEvent";
        String testPayload = "{\"testKey\" : \"testValue\"}";
        EventDTO testEventDTO = new EventDTO(testUserName, testGroupName, testEventType, testPayload);

        //act
        Event testEvent = eventService.transform(testEventDTO);

        //assert
        assertThat(testEvent, isA(mops.gruppen1.domain.events.GroupCreationEvent.class));

    }

    @Test
    void testLoad(){
        //Arrange
        String testUserName = "test_user";
        String testGroupName = "test_group";
        LocalDateTime timestamp = LocalDateTime.now();
        String testEventType = "GroupCreationEvent";
        String testPayload = "{\"testKey\" : \"testValue\"}";

        EventDTO testEventDTO = new EventDTO(testUserName, testGroupName, testEventType, testPayload);
        List<EventDTO> testEventDTOS = new ArrayList<EventDTO>();
        testEventDTOS.add(testEventDTO);

        when(eventService.getEventRepo().findAll()).thenReturn(testEventDTOS);

        //act
         eventService.loadEvents();

        //assert
        assertThat(eventService.getEvents(), hasSize(1));
    }

}
