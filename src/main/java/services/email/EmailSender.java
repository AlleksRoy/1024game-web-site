package services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class EmailSender {

    @Autowired
    private JavaMailSender javaMailSender;

    private int generateCode() {
        int min = 1000000, max = 9999999;
        return (int) Math.floor(Math.random() * (max - min + 1) + min);
    }

    public String sendRegistrationEmail(String email) {
        int code = generateCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("canistra@gmail.com");
        message.setTo(email);
        message.setSubject("\uD83C\uDFAE Your Game1024 Verification Code");
        message.setText(String.join("\n",
                "               🎮 Welcome to Game1024! 🎮",
                "",
                "Hi there,",
                "",
                "Thanks for joining Game1024, your ultimate puzzle adventure!",
                "",
                "Your verification code is:  " + code,
                "",
                "Enter this code on our website to complete your registration.",
                "",
                "If you didn’t request this email, just ignore it — no action is needed.",
                "",
                "Happy gaming,"
        ));
        javaMailSender.send(message);
        return Integer.toString(code);
    }

    public String sendPasswordResetEmail(String email) {
        int code = generateCode();
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("canistra@gmail.com");
        msg.setTo(email);
        msg.setSubject("\uD83D\uDD12 Game1024 Password Reset Request");
        msg.setText(String.join("\n",
                "Hi there!",
                "",
                "We received a request to reset the password for your Game1024 account.",
                "\uD83D\uDD11Your reset code is: " + code,
                "",
                "Enter this code on our Reset Password page to choose a new password.",
                "",
                "If you didn't request a password reset, you can safely ignore this email."
        ));
        javaMailSender.send(msg);
        return Integer.toString(code);
    }
}