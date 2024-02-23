package com.bangtran.security_and_thymeleaf.event.listener;

import com.bangtran.security_and_thymeleaf.event.RegistrationEventComplete;
import com.bangtran.security_and_thymeleaf.registration.token.IVerificationTokenService;
import com.bangtran.security_and_thymeleaf.user.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationEventComplete> {
    private final IVerificationTokenService tokenService;
    private final JavaMailSender mailSender;

    private User user;

    @Override
    public void onApplicationEvent(RegistrationEventComplete event) {
        // 1. Get the user
        user = event.getUser();
        // 2. Tạo token
        String vToken = UUID.randomUUID().toString();
        // 3. Lưu token
        tokenService.saveVerificationTokenForUser(user, vToken);
        // 4. Tạo url xác thực
        String url = event.getConfirmationUrl() + "/registration/verifyEmail?token="+vToken;
        // 5. Gửi email
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Users Verification Service";
        String mailContent = "<p> Hi, "+ user.getFirstName()+ ", </p>"+
                "<p>Thank you for registering with us," + "" +
                "Please, follow the link below to complete your registration.</p>"+
                "<a href=\"" +url+ "\">Verify your email to activate your account</a>"+
                "<p> Thank you <br> Users Registration Portal Service";
        emailMessage(subject, senderName, mailContent, mailSender, user);
    }

    private static void emailMessage(String subject, String senderName,
                                     String mailContent, JavaMailSender mailSender, User theUser)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("bangtran10092002@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
