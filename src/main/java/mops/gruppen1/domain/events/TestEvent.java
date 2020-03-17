package mops.gruppen1.domain.events;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.GroupType;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Fake event, which is used for testing the transformation of DTO´s to a specific eventType and vice versa.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestEvent implements Event {

    private Long eventId;
    private String testUserName;
    private String testGroupName;
    private String testEventType;
    private LocalDateTime timestamp;
    private Long testGroupID;
    private String testGroupDescription;
    private GroupType groupType;
    private Long groupCourseID;


    /**
     * Called by Jackson in order to map JSON-Payload to corresponding fields in TestEvent
     * @param eventParameters
     */
    @JsonProperty("eventParameters")
    private void deserializeNestedParameters(Map<String,Object> eventParameters){
        this.testGroupID = Long.parseLong(eventParameters.get("testGroupId").toString());
        this.testGroupName =(String) eventParameters.get("testGroupName");
        this.timestamp = LocalDateTime.parse(eventParameters.get("testGroupCreation").toString());
        this.testUserName =(String) eventParameters.get("groupCreator");
        this.testGroupDescription =(String) eventParameters.get("groupDescription");
        this.groupType = GroupType.valueOf(eventParameters.get("groupType").toString());
        this.groupCourseID =Long.parseLong(eventParameters.get("groupCourse").toString());
    }

    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
    }
}
