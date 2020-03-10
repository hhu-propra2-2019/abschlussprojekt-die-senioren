package mops.gruppen1.applicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.data.EventRepo;
import mops.gruppen1.domain.events.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to handle and manage events
 */
@Service
public class EventService {

    List<Event> events;
    @Autowired
    private EventRepo eventRepo;

    public void loadEvents() {
        Iterable<EventDTO> eventDTOS = eventRepo.findAll();
        eventDTOS.forEach(e ->  events.add(transform(e)));
    }


    private Event transform(EventDTO eventDTO) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Class<Event> classType = (Class<Event>) Class.forName(eventDTO.getEventType());
            Event event = objectMapper.readValue(eventDTO.getPayload(), classType);
            return event;

        } catch (JsonProcessingException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }


}
