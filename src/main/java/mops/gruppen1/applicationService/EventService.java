package mops.gruppen1.applicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.events.IEvent;
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
    private List<IEvent> events;
    private final String EventClassPath = "mops.gruppen1.domain.events.";

    /**
     * Init EventService
     * @param eventRepo the CrudRepository
     */
    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
        events = new ArrayList<>();
    }

    /**
     * Load all events from the EventRepo
     * TODO: 16.03.20 Investigate if we have to sort eventDTOs by id at this point
     */
    public void loadEvents() {

        //Get all EventDTO´s from EventRepo
        Iterable<EventDTO> eventDTOS = eventRepo.findAll();

        //Fill list of events
        eventDTOS.forEach(e ->  {
            IEvent event = transform(e);
            events.add(event);
        });
    }

    /**
     * Transformation of generic EventDTO to specific EventType
     * @param eventDTO the DTO to be transformed
     * @return Returns initialized Event
     */
    public IEvent transform(EventDTO eventDTO) {

        //Jackson ObjectMapper
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            //Get specifc classType for eventDTO
            Class<IEvent> classType = (Class<IEvent>) Class.forName(EventClassPath + eventDTO.getEventType());

            //Deserialize Json-Payload
            IEvent event = objectMapper.readValue(eventDTO.getPayload(), classType);

            return event;

        } catch (JsonProcessingException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * stores the EventDTO to the CrudRepository EventRepo
     * @param eventDTO the EventDTO to be saved
     */
    public void saveToRepository(EventDTO eventDTO) {
        eventRepo.save(eventDTO);
    }


    /**
     * creates the EventDTO
     * @param userName necessary parameter for all EventDTOs
     * @param groupID necessary parameter for all EventDTOs
     * @param timestamp necessary parameter for all EventDTOs
     * @param eventType necessary parameter for all EventDTOs
     * @param event necessary parameter for all EventDTOs
     * @return the created EventDTO by objectMapper and Json
     */
    public EventDTO createEventDTO(String userName, String groupID, LocalDateTime timestamp, String eventType, IEvent event) {
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
