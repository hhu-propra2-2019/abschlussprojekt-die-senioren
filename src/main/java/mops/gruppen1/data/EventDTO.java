package mops.gruppen1.data;

import lombok.Data;

import javax.persistence.*;

/**
 * DTO for Events
 */
@Data
@Entity
public class EventDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_ref")
    private String user;
    @Column(name = "group_ref")
    private String group;
    private String eventType;
    @Column(length = 2000)
    private String payload;

}
