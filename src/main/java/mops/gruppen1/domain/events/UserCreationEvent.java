package mops.gruppen1.domain.events;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;
import mops.gruppen1.domain.Username;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * create new instance of class User.
 * Add to HashMap users
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserCreationEvent implements IEvent {
    private String username;

    @Override
    public void execute(HashMap<String, List<Membership>> groupToMembers, HashMap<String,
            List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {
        Username username = new Username(this.username);
        User user = new User(username);
        users.put(this.username, user);
        userToMembers.put(this.username, new ArrayList<>());
    }
}
