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
import mops.gruppen1.domain.Username;
import mops.gruppen1.domain.events.Event;
import mops.gruppen1.domain.events.GroupCreationEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Service to manage the group entities
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class GroupService {
    private HashMap<Group, List<Membership>> groupToMembers;
    private HashMap<User, List<Membership>> userToMembers;
    private HashSet<User> users;
    private HashSet<Group> groups;
    @Autowired
    EventService events;


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
        LocalDateTime timestamp = LocalDateTime.now();

        ObjectMapper objectMapper = new ObjectMapper();
        String payload = "";
        try {
            payload = objectMapper.writeValueAsString(groupCreationEvent);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        EventDTO eventDTO = new EventDTO(userName, groupID, timestamp, "GroupCreationEvent", payload);
    }
}
