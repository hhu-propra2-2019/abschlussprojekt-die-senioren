package mops.gruppen1.data.daos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * object containing outbound attributes of class User
 * is given to other services via RestService and RestController
 */
@Getter
@AllArgsConstructor
public class UserDAO {
    private String userName;

}
