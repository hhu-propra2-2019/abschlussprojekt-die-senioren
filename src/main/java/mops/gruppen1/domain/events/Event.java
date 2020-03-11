package mops.gruppen1.domain.events;

import mops.gruppen1.applicationService.GroupService;

public interface Event {
    public void execute(GroupService groupService);
}
