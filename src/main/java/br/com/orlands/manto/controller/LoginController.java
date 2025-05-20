package br.com.orlands.manto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.orlands.manto.domain.UserDomain;
import br.com.orlands.manto.service.UserService;

@Controller
public class LoginController {

    private PasswordEncoder passwordEncoder;
    private UserService userService;

    @Autowired
    public LoginController(PasswordEncoder passwordEncoder, UserService userService) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/cadastro")
    public String cadastrar(@ModelAttribute UserDomain user,
            @RequestParam("confirmPassword") String confirmPassword,
            Model model) {
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            model.addAttribute("erro", "Email já cadastrado.");
            return "/login";
        }

        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("erro", "As senhas não conferem.");
            return "/login";
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.save(user);
        return "index";
    }

}
