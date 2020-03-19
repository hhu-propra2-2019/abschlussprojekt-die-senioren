package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import mops.gruppen1.domain.Forum;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * Add a link of the external ForumService to the Group
 * TODO: Add check for already existing Forum to Group Service
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class ForumCreationEvent implements IEvent {
    private String groupId;
    private String forumLink;
    private String createdBy;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Group group = groups.get(groupId);
        Forum forum = new Forum(forumLink);
        group.setForum(forum);
    }
}
