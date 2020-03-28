package mops.gruppen1.data.daos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Object containing outbound attributes of class User.
 * Is given to other services via RestService and RestController.
 */
@Getter
@AllArgsConstructor
public class UserDAO {
    private String userName;

}
