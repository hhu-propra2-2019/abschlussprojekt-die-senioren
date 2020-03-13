package mops.gruppen1.applicationService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import mops.gruppen1.data.EventDTO;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import mops.gruppen1.domain.events.Event;
import mops.gruppen1.domain.events.GroupCreationEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Service to manage the group entities
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class GroupService {
    @Autowired
    EventService events;
    private HashMap<Group, List<Membership>> groupToMembers;
    private HashMap<User, List<Membership>> userToMembers;
    private HashSet<User> users;
    private HashSet<Group> groups;

    public void init() {
        events.loadEvents();
        List<Event> eventList = events.getEvents();
        eventList.stream().forEach(e -> e.execute(
                groupToMembers,
                userToMembers,
                users,
                groups
        ));
    }

    public void createGroupCreationEvent(String userName, Group group) {
        GroupCreationEvent groupCreationEvent = new GroupCreationEvent("testValue");
        groupCreationEvent.execute(groupToMembers, userToMembers, users, groups);

        String groupID = group.getGroupId().toString();

        EventDTO groupCreationEventDTO = createEventDTO(userName, groupID, "GroupCreationEvent", groupCreationEvent);
    }

    private EventDTO createEventDTO(String userName, String groupID, String eventType, Event event) {
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
