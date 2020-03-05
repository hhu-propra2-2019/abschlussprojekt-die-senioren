package mops.gruppen1.domain;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.UUID;

@Entity
public class Membership {

    @Id
    private UUID id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;
}
