package mops.gruppen1.domain.events;

public interface Event {
    public void execute(EventService eventService);

    //TODO Add EventService class as an application service (wraps all data structures)
}
