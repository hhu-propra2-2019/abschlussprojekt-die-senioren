package mops.gruppen1.domain.events;

import mops.gruppen1.applicationService.EventService;

public interface Event {
    public void execute(EventService eventService);
}
