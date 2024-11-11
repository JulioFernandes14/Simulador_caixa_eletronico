package br.com.si.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.si.model.Conta;
import br.com.si.model.Users;
import br.com.si.repository.UsersRepository;

@Controller
public class UsersController {

    @Autowired
    private UsersRepository usersRepository;
    
    @GetMapping("/cadastro")
    public String cadastroUsers(Model model) {
        Users users = new Users();
        model.addAttribute("users", users);
        return "usersform";
    }
    
    @PostMapping("/cadastrar")
    public String cadastrarusuario(@ModelAttribute("users") Users users, BindingResult result, Model model) {
        try {
            users.setPassword(users.criptografia(users.getPassword()));
            
            Conta conta = new Conta();
            conta.setSaldo(50.0); 
            users.setConta(conta);
            
            Users resp = usersRepository.save(users);
            if (resp == null) {
                throw new Exception("Não gravado");
            }
            
            model.addAttribute("message", "Cadastro realizado com sucesso");
            return "usersform";
        } catch (Exception ex) {
            model.addAttribute("message", "Erro: " + ex.getMessage());
            return "usersform"; 
        }
    }
}
