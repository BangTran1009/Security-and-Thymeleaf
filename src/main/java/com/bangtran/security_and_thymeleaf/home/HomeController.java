package com.bangtran.security_and_thymeleaf.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping()
    public String Home() {
        return "home";
    }
    @GetMapping("login")
    public String Login() {
        return "login";
    }

    @GetMapping("/error")
    public String Error() {
        return "error";
    }
}
