package pecunia_22.controllers.viewControllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ContinentController {

    @GetMapping("/con")
    @ResponseBody
    public String index() {
        System.out.println("\t\t ----------PECUNIA 22-----------");
         return "PECUNIA 22";
    }
}
