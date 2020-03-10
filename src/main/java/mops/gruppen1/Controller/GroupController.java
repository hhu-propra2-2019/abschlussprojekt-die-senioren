package mops.gruppen1.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
// Leave commented until stylguide is fixed for this route
//@RequestMapping("/gruppen1")
public class GroupController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
