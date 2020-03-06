package mops.gruppen1.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class GroupDescription {
    String description;
}
