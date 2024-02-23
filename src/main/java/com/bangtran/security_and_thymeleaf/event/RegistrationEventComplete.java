package com.bangtran.security_and_thymeleaf.event;

import com.bangtran.security_and_thymeleaf.user.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationEventComplete extends ApplicationEvent {
    private User user;
    private String confirmationUrl;
    public RegistrationEventComplete(User user, String confirmationUrl) {
        super(user);
        this.user = user;
        this.confirmationUrl = confirmationUrl;
    }
}
