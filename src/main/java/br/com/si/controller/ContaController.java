package br.com.si.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.si.model.Users;
import br.com.si.repository.UsersRepository;
import jakarta.servlet.http.HttpSession;

@Controller
public class ContaController {

    @Autowired
    private HttpSession session;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/deposito")
    public String deposito(Model model) {
    	Users user = (Users) session.getAttribute("user");
	    model.addAttribute("user", user);
        return "deposito";
    }

    @PostMapping("/depositar")
    public String depositar(@RequestParam double valor, Model model) {

        Users user = (Users) session.getAttribute("user");

        if (user != null) {
            user.getConta().setSaldo(user.getConta().getSaldo() + valor);

            usersRepository.save(user);

            session.setAttribute("user", user);

            model.addAttribute("mensagem", "Depósito realizado com sucesso! Seu novo saldo é: R$" + user.getConta().getSaldo());
        } else {
            model.addAttribute("mensagem", "Conta não encontrada. Faça login novamente.");
        }

        return "resultadoDeposito";
    }
    
    @GetMapping("/saque")
    public String saque(Model model) {
    	Users user = (Users) session.getAttribute("user");
	    model.addAttribute("user", user);
        return "saque";
    }
    
    @PostMapping("/sacar")
    public String sacar(@RequestParam double valor, Model model) {

        Users user = (Users) session.getAttribute("user");

        if (user != null) {
           
        	if (user.getConta().getSaldo() >= valor) {
        		
        		 user.getConta().setSaldo(user.getConta().getSaldo() - valor);

                 usersRepository.save(user);

                 session.setAttribute("user", user);
                 
                 model.addAttribute("mensagem", "Saque realizado com sucesso! Seu novo saldo é: R$" + user.getConta().getSaldo());
        		
        	}else {
        		model.addAttribute("mensagem", "Saldo insuficiente");
        	}

            
        } else {
            model.addAttribute("mensagem", "Conta não encontrada. Faça login novamente.");
        }

        return "resultadoDeposito";
    }
    
    @GetMapping("/transferencia")
    public String mostrarPaginaTransferencia(Model model) {
    	Users user = (Users) session.getAttribute("user");
	    model.addAttribute("user", user);
        return "transferencia";
    }

    @PostMapping("/transferir")
    public String processarTransferencia(@RequestParam double valor, Model model) {
    	Users user = (Users) session.getAttribute("user");
	    model.addAttribute("user", user);
        model.addAttribute("valor", valor);
        return "formularioConta";
    }

    @PostMapping("/transferir/confirmar")
    public String confirmarTransferencia(@RequestParam double valor, @RequestParam Long contaDestinoId, Model model) {
        Users user = (Users) session.getAttribute("user");

        if (user != null) {
        	
            Integer idDestino = contaDestinoId.intValue();
            Users contaDestino = usersRepository.findById(idDestino).orElse(null);

            if (contaDestino == null) {
                model.addAttribute("mensagem", "Transferência falhou. Conta de destino não encontrada.");
                return "resultadoTransferencia";
            }

            if (user.getConta().getSaldo() < valor) {
                model.addAttribute("mensagem", "Transferência falhou. Saldo insuficiente.");
                return "resultadoTransferencia";
            }

            user.getConta().setSaldo(user.getConta().getSaldo() - valor);
            contaDestino.getConta().setSaldo(contaDestino.getConta().getSaldo() + valor);
            
            usersRepository.save(user);
            usersRepository.save(contaDestino);

            model.addAttribute("mensagem", "Transferência de " + valor + " realizada com sucesso para: " + contaDestino.getName() + " " + contaDestino.getSobrenome());
        } else {
            model.addAttribute("mensagem", "Conta não encontrada. Faça login novamente.");
        }

        return "resultadoTransferencia";
    }
 
    
}
