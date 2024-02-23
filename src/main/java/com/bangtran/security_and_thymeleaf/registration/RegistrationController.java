package com.bangtran.security_and_thymeleaf.registration;

import com.bangtran.security_and_thymeleaf.event.RegistrationEventComplete;
import com.bangtran.security_and_thymeleaf.registration.token.VerificationToken;
import com.bangtran.security_and_thymeleaf.registration.token.VerificationTokenService;
import com.bangtran.security_and_thymeleaf.user.IUserService;
import com.bangtran.security_and_thymeleaf.user.User;
import com.bangtran.security_and_thymeleaf.util.UrlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private final IUserService userService;
    private final ApplicationEventPublisher publisher;
    private final VerificationTokenService tokenService;

    @GetMapping("/registration-form")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new RegistrationRequest());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") RegistrationRequest registrationRequest,
                               HttpServletRequest request) {
        User user = userService.RegisterUser(registrationRequest);
        // Gọi sự kiện xác thực email
        publisher.publishEvent(new RegistrationEventComplete(user, UrlUtil.getApplicationUrl(request)));
        return "redirect:/registration/registration-form?success";
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        Optional<VerificationToken> theToken = tokenService.findByToken(token);
        if (theToken.isPresent() && theToken.get().getUser().isEnabled()) {
            return "redirect:/login?verified";
        }
        String verificationResult = tokenService.validateToken(token);
        switch (verificationResult.toLowerCase()) {
            case "expired":
                return "redirect:/error?expired";
            case "valid":
                return "redirect:/login?valid";
            default:
                return "redirect:/error?invalid";
        }
    }
}
