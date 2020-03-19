package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Appointment;
import mops.gruppen1.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AppointmentDeletionEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test AppointmentDeletionEvent")
    @Test
    void execute() {
        //arrange
        Group groupOne = testSetup.groupOne;
        String groupId = testSetup.groupOne.getGroupId().toString();
        groupOne.setAppointment(new Appointment("http://www.hierkommstduzudeinenterminen.de"));
        AppointmentDeletionEvent appointmentDeletionEvent = new AppointmentDeletionEvent(groupId);

        //act
        appointmentDeletionEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(groupOne.getAppointment()).isNull();
    }
}
