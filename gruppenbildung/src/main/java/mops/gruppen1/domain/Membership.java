package mops.gruppen1.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
@Table(name = "membership1")
public class Membership {

    // @EmbeddedId
    @Id
    // @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Column(name = "membership_id")MembershipID
    private UUID memberid;

    @ManyToOne(cascade = CascadeType.ALL)
    //   @MapsId("userId")
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    @ManyToOne(cascade = CascadeType.ALL)
    @MapsId("groupId")
    @JoinColumn(name = "group_id", referencedColumnName = "groupId")
    private Group group;
}
