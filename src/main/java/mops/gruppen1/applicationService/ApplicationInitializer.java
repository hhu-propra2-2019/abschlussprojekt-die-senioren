package mops.gruppen1.applicationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationInitializer implements ApplicationListener<ApplicationReadyEvent> {

    private GroupService groupService;

    @Autowired
    public ApplicationInitializer(GroupService groupService) {
        this.groupService = groupService;
    }

    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        groupService.init();

    }

}
