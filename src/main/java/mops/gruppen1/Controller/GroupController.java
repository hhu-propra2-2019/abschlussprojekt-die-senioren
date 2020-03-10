package mops.gruppen1.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/gruppen1")
public class GroupController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
