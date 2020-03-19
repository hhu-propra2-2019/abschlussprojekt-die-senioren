package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Delete link of the external MaterialService in the Group
 * or change representation
 */
@Getter
@AllArgsConstructor
public class MaterialDeletionEvent implements IEvent {
    private String groupId;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        group.setMaterial(null);
    }
}
