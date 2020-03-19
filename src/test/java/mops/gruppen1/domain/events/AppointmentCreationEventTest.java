package mops.gruppen1.domain.events;

import mops.gruppen1.domain.Group;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentCreationEventTest {

    private TestSetup testSetup;

    @BeforeEach
    void setup() {
        this.testSetup = new TestSetup();
    }

    @Tag("EventTest")
    @DisplayName("Test AppointmentCreationEvent")
    @Test
    void execute() {
        //arrange
        Group groupOne = testSetup.groupOne;
        String groupId = testSetup.groupOne.getGroupId().toString();
        AppointmentCreationEvent appointmentCreationEvent = new AppointmentCreationEvent( groupId,
                 "http://www.hierkommstduzudeinenterminen.de",
                "VonWemhabenwadas");

        //act
        appointmentCreationEvent.execute(testSetup.groupToMembers, testSetup.userToMembers, testSetup.users, testSetup.groups);

        //assert
        assertThat(groupOne.getAppointment()).isNotNull();
        assertThat(groupOne.getAppointment().getLink()).isEqualTo("http://www.hierkommstduzudeinenterminen.de");

    }
}