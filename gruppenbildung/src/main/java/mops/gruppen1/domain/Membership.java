package mops.gruppen1.domain;

import javax.persistence.ManyToOne;

public class Membership {

    @ManyToOne
    private User user;

    @ManyToOne
    private Group group;
}
