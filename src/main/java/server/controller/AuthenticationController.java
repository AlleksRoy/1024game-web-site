package server.controller;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import entity.Users;
import services.comment.CommentService;
import services.rating.RatingService;
import services.score.ScoreService;
import services.user.UserService;

import java.util.Date;
import java.util.Objects;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    ScoreService scoreService;

    @Autowired
    CommentService commentService;

    @Autowired
    RatingService ratingService;

    @Getter
    private String UserName;

    @RequestMapping("/register")
    public String getRegisterPage() {
        return "RegisterPage1024";
    }

    @PostMapping("/register")
    public String register(@RequestParam(required = false) String email, @RequestParam(required = false) String login, @RequestParam(required = false) String password, RedirectAttributes ra) {
        if (userService.addUser(new Users(login, email, password, "1024", new Date()))) {
            ra.addFlashAttribute("registrationSuccess", "You have been registered successfully");
        } else if (userService.isLoginUsed("1024", login) && userService.isEmailUsed("1024", email)) {
            ra.addFlashAttribute("accountError", "You have already registered");
        } else if (userService.isLoginUsed("1024", login)) {
            ra.addFlashAttribute("loginError", "Login already used");
        } else if (userService.isEmailUsed("1024", email)) {
            ra.addFlashAttribute("emailError", "Email already used");
        }
        return "redirect:/register";
    }

    @RequestMapping("/login")
    public String getLoginPage() {
        return "LoginPage1024";
    }

    @PostMapping("/login")
    public String login(@RequestParam(required = false) String login, @RequestParam(required = false) String password, RedirectAttributes ra) {
        if (Objects.equals(userService.getUserPassword("1024", login), password)) {
            UserName = login;
            return "redirect:/";
        } else if (userService.isLoginUsed("1024", login) && !Objects.equals(userService.getUserPassword("1024", login), password)) {
            ra.addFlashAttribute("passwordError", "Wrong password");
        } else if (!userService.isLoginUsed("1024", login)) {
            ra.addFlashAttribute("loginError", "You are not registered");
        }
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        model.addAttribute("username", UserName);
        model.addAttribute("registrationDate", userService.getUresRegestationDate("1024", UserName));
        return "UserPage";
    }

    @PostMapping("/profile/username")
    public String changeUsername(@RequestParam("newUsername") String newUsername, RedirectAttributes ra) {
        if (isUserLogged()) {
            String oldLogin = UserName;
            String game = "1024";
            if (!userService.isLoginUsed(game, newUsername)) {
                userService.changeLogin(game, oldLogin, newUsername);
                commentService.updatePlayerName(oldLogin, newUsername);
                ratingService.updatePlayerName(oldLogin, newUsername);
                scoreService.updatePlayerName(oldLogin, newUsername);
                UserName = newUsername;
                ra.addFlashAttribute("usernameSuccess", "Username updated");
            } else {
                ra.addFlashAttribute("usernameError", "That login is already taken");
            }
        }
        return "redirect:/profile";
    }

    @PostMapping("/profile/password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, RedirectAttributes ra) {
        if (isUserLogged()) {
            String login = UserName;
            String game = "1024";
            if (oldPassword.matches(userService.getUserPassword("1024", UserName))) {
                userService.changePassword(game, login, newPassword);
                ra.addFlashAttribute("passwordSuccess", "Password updated");
            } else {
                ra.addFlashAttribute("passwordError", "Old password is incorrect");
            }
        }
        return "redirect:/profile";
    }

    @GetMapping("/reset-password")
    public String getResetPasswordPage() {
        return "ResetPasswordPage1024";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String login, @RequestParam String newPassword, RedirectAttributes ra) {
        String game = "1024";
        if (userService.isLoginUsed(game, login)) {
            userService.changePassword(game, login, newPassword);
            ra.addFlashAttribute("passwordResetSuccess", "Password successfully reset");
            return "redirect:/login";
        } else {
            ra.addFlashAttribute("loginError", "Login not found");
            return "redirect:/reset-password";
        }
    }

    @RequestMapping("/logout")
    public String logout() {
        UserName = null;
        return "redirect:/";
    }

    public boolean isUserLogged() {
        return UserName != null;
    }
}
