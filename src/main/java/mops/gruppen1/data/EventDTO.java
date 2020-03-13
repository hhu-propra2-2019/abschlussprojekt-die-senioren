package mops.gruppen1.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO for Events
 */
@Data
@Entity
public class EventDTO {

    public EventDTO(String user, String group, String eventType, String payload) {
        this.user = user;
        this.group = group;
        this.eventType = eventType;
        this.payload = payload;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_ref")
    private String user;
    @Column(name = "group_ref")
    private String group;
    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime timestamp;
    private String eventType;
    @Column(length = 2000)
    private String payload;

}
