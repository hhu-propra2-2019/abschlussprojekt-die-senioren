package mops.gruppen1.Controller.Interaction;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gruppen1")
public class InteractionController {

    @GetMapping("/userInGroup")
    public String userInGroup(@RequestParam(value = "name", defaultValue = "nope") String name) {
        // if default-value => request is not valid, username must be given
        //Check if user is in Group
        //return yes/no?
        return name;
    }

    @GetMapping("/isAdmin")
    public String isAdmin(@RequestParam(value = "name", defaultValue = "noId") String name,
                          @RequestParam(value = "group", defaultValue = "noGroupID") String group) {
        // if default-value => request is not valid, username must be given
        // check if user ist admin in Group
        //return yes/no?
        return name;
    }

}
