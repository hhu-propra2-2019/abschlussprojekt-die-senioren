package mops.gruppen1.domain.events;

import lombok.Data;
import mops.gruppen1.applicationService.GroupService;

@Data
public class GroupCreationEvent implements Event {

    public String testKey;

    @Override
    public void execute(GroupService groupService) {

    }
}
