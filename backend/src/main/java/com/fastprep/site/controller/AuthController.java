package com.fastprep.site.controller;

import com.fastprep.site.model.User;
import com.fastprep.site.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/signup")
    public String processSignup(@RequestParam String fullName,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               HttpSession session,
                               Model model) {
        
        // Basic validation
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "signup";
        }
        
        try {
            User user = userService.createUser(email, fullName, password);
            
            // Store user in session
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userEmail", user.getEmail());
            
            return "redirect:/dashboard";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "signup";
        }
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {
        
        User user = userService.authenticateUser(email, password);
        if (user != null) {
            session.setAttribute("userId", user.getId());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("userEmail", user.getEmail());
            
            return "redirect:/dashboard";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}