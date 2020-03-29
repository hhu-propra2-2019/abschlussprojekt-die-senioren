package mops.gruppen1.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MembershipRequestMessage {
    private String message;


    @Override
    public String toString() {
        return message;
    }
}
