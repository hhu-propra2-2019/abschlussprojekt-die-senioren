package mops.gruppen1.data.daos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Object containing outbound attributes of class Group.
 * Is given to other services via RestService and RestController.
 */
@Getter
@AllArgsConstructor
public class GroupDAO {
    private String groupId;
    private String groupName;
    private String course;
    private String groupDescription;
    private String status;

    public GroupDAO(String groupId, String groupName, String groupDescription, String status) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupDescription = groupDescription;
        this.status = status;
    }
}
