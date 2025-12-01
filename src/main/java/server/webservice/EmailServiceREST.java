package server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import services.email.EmailSender;

@RestController
@RequestMapping("/api/send-email")
public class EmailServiceREST {
    @Autowired
    private EmailSender emailSender;

    @GetMapping("/verification/{email}")
    public String sendRegistrationEmail(@PathVariable String email) {
        return emailSender.sendRegistrationEmail(email);
    }

    @GetMapping("/reset/{email}")
    public String sendPasswordResetEmail(@PathVariable String email) {
        return emailSender.sendPasswordResetEmail(email);
    }
}