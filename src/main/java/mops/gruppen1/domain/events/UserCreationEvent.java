package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import mops.gruppen1.domain.Membership;
import mops.gruppen1.domain.User;

import java.util.HashMap;
import java.util.List;

/**
 * create new instance of class User
 * Add to HashSet users
 * DELETE THIS EVENT?
 */
public class UserCreationEvent implements IEvent {


    @Override
    public void execute(HashMap<Group, List<Membership>> groupToMembers, HashMap<User, List<Membership>> userToMembers, HashMap<String, User> users, HashMap<String, Group> groups) {

    }
}
