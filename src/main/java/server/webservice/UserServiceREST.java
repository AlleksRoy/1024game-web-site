package server.webservice;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import entity.Users;
import services.user.UserService;

@RestController
@RequestMapping("/api/user")
@AllArgsConstructor
public class UserServiceREST {
    @Autowired
    private UserService userService;

    @PostMapping()
    public void addUser(@RequestBody Users user) {
        userService.addUser(user);
    }

    @GetMapping("/{game}/{login}")
    public String getUserPassword(@PathVariable String game, @PathVariable String login) {
        return userService.getUserPassword(game, login);
    }

    @GetMapping("/{game}/isLoginUsed/{login}")
    public boolean isLoginUsed(@PathVariable String game, @PathVariable String login) {
        return userService.isLoginUsed(game, login);
    }

    @GetMapping("/{game}/isEmailUsed/{email}")
    public boolean isEmailUsed(@PathVariable String game, @PathVariable String email) {
        return userService.isEmailUsed(game, email);
    }

    @GetMapping("/{game}/getUserEmail/{login}")
    public String getUserEmail(@PathVariable String game, @PathVariable String login) {
        return userService.getUserEmail(game, login);
    }
}
