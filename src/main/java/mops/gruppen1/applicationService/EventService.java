package mops.gruppen1.applicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.events.Event;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service to handle and manage events
 */
@Getter
@Service
public class EventService {

    final EventRepo eventRepo;
    private List<Event> events;
    private final String EventClassPath = "mops.gruppen1.domain.events.";

    /**
     * Init EventService
     * @param eventRepo
     */
    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
        events = new ArrayList<Event>();
    }

    /**
     * Load all events from the EventRepo
     */
    public void loadEvents() {

        //Get all EventDTO´s from EventRepo
        Iterable<EventDTO> eventDTOS = eventRepo.findAll();

        /**
         * @// TODO: 16.03.20 Investigate if we have to sort eventDTOs by id at this point
         */

        //Fill list of events
        eventDTOS.forEach(e ->  {
            Event event = transform(e);
            events.add(event);
        });
    }

    /**
     * Transformation of generic EventDTO to specifc EventType
     * @param eventDTO
     * @return Returns initialized Event
     */
    public Event transform(EventDTO eventDTO) {

        //Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            //Get specifc classType for eventDTO
            Class<Event> classType = (Class<Event>) Class.forName(EventClassPath + eventDTO.getEventType());

            //Deserialize Json-Payload
            Event event = objectMapper.readValue(eventDTO.getPayload(), classType);

            return event;

        } catch (JsonProcessingException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveToRepository(EventDTO eventDTO) {
        eventRepo.save(eventDTO);
    }


    EventDTO createEventDTO(String userName, String groupID, String eventType, Event event) {
        LocalDateTime timestamp = LocalDateTime.now();
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return new EventDTO(userName, groupID, timestamp, eventType, payload);
    }


}
