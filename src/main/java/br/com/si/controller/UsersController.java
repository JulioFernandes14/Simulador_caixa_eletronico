package br.com.si.controller;

import java.util.Optional;

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
import jakarta.servlet.http.HttpSession;

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
    
    @GetMapping("/dados")
    public String dados(Model model, HttpSession session) {
    	Users user = (Users) session.getAttribute("user");
	    model.addAttribute("user", user);
        return "dados";
    }
    
    @PostMapping("/editar")
    public String editarusuario(@ModelAttribute("user") Users user, BindingResult result, Model model, HttpSession session) {
        try { 
        	Users sessionUser = (Users) session.getAttribute("user");
            user.setId(sessionUser.getId());
            user.setPassword(sessionUser.getPassword());
            Users resp = usersRepository.save(user);
            session.setAttribute("user", user);
            if (resp == null) {
                throw new Exception("Erro na edição");
            }
            
            model.addAttribute("mensagem", "Edição de dados realizada com sucesso");
            return "editar";
        } catch (Exception ex) {
            model.addAttribute("mensagem", "Erro: " + ex.getMessage());
            return "editar"; 
        }
    }
    
    @GetMapping("/deletar")
    public String deletarusuario(Model model, HttpSession session) {
    	try { 
    		Users user = (Users) session.getAttribute("user");
    		usersRepository.deleteById(user.getId());
    		Optional<Users> deletedUser = usersRepository.findById(user.getId());
    		if (deletedUser.isPresent()) {
                throw new Exception("Erro na exclusão");
            }
	    	model.addAttribute("mensagem", "Exclusão de usuário realizada com sucesso");
	        session.invalidate();
	        return "deletar";
	    }catch (Exception ex) {
	    	model.addAttribute("mensagem", "Erro: " + ex.getMessage());
	        return "deletar"; 
		}

    }
    
}
