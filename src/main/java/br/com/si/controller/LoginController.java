package br.com.si.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.si.model.Users;
import br.com.si.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("users", new Users());
        return "loginform";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("users") Users users, Model model, HttpSession session) {
        try {
            Users foundUser = usersRepository.findByEmail(users.getEmail());

            if (foundUser != null && foundUser.getPassword().equals(users.criptografia(users.getPassword()))) {
                model.addAttribute("message", "Bem-vindo, " + foundUser.getName() + "!");
                session.setAttribute("user", foundUser);
                return "redirect:/conta";
            } else {
                model.addAttribute("message", "Email ou senha incorretos.");
                return "loginform";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Erro: " + e.getMessage());
            return "loginform";
        }
    }

	@GetMapping("/conta")
		public String contaPage(Model model, HttpSession session) {
	    Users user = (Users) session.getAttribute("user");
	    model.addAttribute("user", user);
	    return "conta";
	}
	
	@GetMapping("/logout")
		public String logoutPage(HttpSession session) {
	    session.invalidate();
	    return "redirect:/login";
	}
}
