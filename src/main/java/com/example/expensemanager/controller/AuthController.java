package com.example.expensemanager.controller;

import com.example.expensemanager.dto.UserDto;
import com.example.expensemanager.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @GetMapping({"/", "/login"})
    public String showLoginPage() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            return "login";
        }
        return "redirect:/expenses";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        System.out.println("User details: " + userDto);
        if (result.hasErrors()) {
            return "register";
        }
        userService.save(userDto);
        model.addAttribute("successMsg", true);
        return "response";
    }
}
