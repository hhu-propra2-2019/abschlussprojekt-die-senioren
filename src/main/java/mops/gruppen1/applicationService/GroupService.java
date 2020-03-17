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
import mops.gruppen1.domain.events.MembershipAssignmentEvent;
import mops.gruppen1.domain.events.GroupDeletionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
@Service
public class GroupService {
    @Autowired //TODO: Remove Autowired ?
    EventService events;
    private HashMap<Group, List<Membership>> groupToMembers;
    private HashMap<User, List<Membership>> userToMembers;
    private HashMap<String, Group> groups;
    private HashMap<String, User> users;

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

    public void createGroupCreationEvent(String groupDescription, String groupName, String groupCourse, String groupCreator) {
        GroupCreationEvent groupCreationEvent = new GroupCreationEvent(groupDescription, groupName, groupCourse, groupCreator);
        groupCreationEvent.execute(groupToMembers, userToMembers, users, groups);
        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupCreationEventDTO = events.createEventDTO(groupCreator, groupCreationEvent.getGroupID(), timestamp, "GroupCreationEvent", groupCreationEvent);
        events.saveToRepository(groupCreationEventDTO);
    }
  
    public void assignMembership(String userName, Group group, String membershipType) {


        /* todo check if GroupType is PUBLIC and GroupStatus is 'active'
            todo check if group is assigned to a module/course, user has to be assigned to it as well
             todo check if user is already a member of the group
             todo check if group is part of hashmaps
         */
        String groupID = group.getGroupId().toString();

        MembershipAssignmentEvent membershipAssignmentEvent = new MembershipAssignmentEvent(groupID, userName, membershipType);
        membershipAssignmentEvent.execute(groupToMembers, userToMembers, users, groups);
        
        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO membershipAssignmentEventDTO = events.createEventDTO(userName, groupID, timestamp, "MembershipAssignmentEvent", membershipAssignmentEvent);

        events.saveToRepository(membershipAssignmentEventDTO);
    }
  
    public void createGroupDeletionEvent(String userName, UUID groupID) {
        String groupId = groupID.toString();
        GroupDeletionEvent groupDeletionEvent = new GroupDeletionEvent(groupId, userName);
        groupDeletionEvent.execute(groupToMembers, userToMembers, users, groups);
        
        LocalDateTime timestamp = LocalDateTime.now();

        EventDTO groupDeletionEventDTO = events.createEventDTO(userName, groupId, timestamp, "GroupDeletionEvent", groupDeletionEvent);

        events.saveToRepository(groupDeletionEventDTO);      
      
    }





}
