package mops.gruppen1.data;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * DTO for Events
 */
@Data
@Entity
@Table(name="events")
public class EventDTO {

    public EventDTO(String user, String group, LocalDateTime timestamp, String eventType, String payload) {
        this.user = user;
        this.group = group;
        this.timestamp = timestamp;
        this.eventType = eventType;
        this.payload = payload;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "userName")
    private String user;
    @Column(name = "groupId")
    private String group;
    @Column(columnDefinition = "TIMESTAMP", name="eventTimeStamp")
    private LocalDateTime timestamp;
    private String eventType;
    @Column(length = 2000)
    private String payload;

}
