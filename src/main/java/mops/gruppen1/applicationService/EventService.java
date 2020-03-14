package mops.gruppen1.applicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.events.Event;
import mops.gruppen1.domain.events.GroupCreationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
        events = new ArrayList<Event>();
    }

    public void loadEvents() {
        Iterable<EventDTO> eventDTOS = eventRepo.findAll();
        eventDTOS.forEach(e ->  {
            Event event = transform(e);
            events.add(event);
        });
    }


    public Event transform(EventDTO eventDTO) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {

            Class<Event> classType = (Class<Event>) Class.forName(EventClassPath + eventDTO.getEventType());
            Event event = objectMapper.readValue(eventDTO.getPayload(), classType);
            return event;

        } catch (JsonProcessingException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }
}
