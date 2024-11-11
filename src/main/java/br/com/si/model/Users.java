package br.com.si.model;

import java.security.MessageDigest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Users {

	 @Id
	 @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private Integer id;
	    
	 private String name;
	 private String sobrenome;
	    
	 @Column(unique = true)
	 private String cpf;
	 
	 @Column(unique = true)
	 private String telefone;
	 
	 private String cep;
	 
	 @Column(unique = true)
	 private String email;
	 
	 private String password;

	 @OneToOne(cascade = CascadeType.ALL)
	 @JoinColumn(name = "conta_id", referencedColumnName = "id")
	 private Conta conta;
	
	 public Users() {
		this.conta = new Conta();
	}
	
	 
	 
	 public Users(Integer id, String nome, String sobrenome, String cpf, String telefone, String cep, String email,
			String password) {
		super();
		this.id = id;
		this.name = nome;
		this.sobrenome = sobrenome;
		this.cpf = cpf;
		this.telefone = telefone;
		this.cep = cep;
		this.email = email;
		this.password = password;
		this.conta = new Conta();
	}

	 

	public Integer getId() {
		return id;
	}



	public void setId(Integer id) {
		this.id = id;
	}



	public String getName() {
		return name;
	}



	public void setName(String nome) {
		this.name = nome;
	}



	public String getSobrenome() {
		return sobrenome;
	}



	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}



	public String getCpf() {
		return cpf;
	}



	public void setCpf(String cpf) {
		this.cpf = cpf;
	}



	public String getTelefone() {
		return telefone;
	}



	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}



	public String getCep() {
		return cep;
	}



	public void setCep(String cep) {
		this.cep = cep;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public Conta getConta() {
		return conta;
	}



	public void setConta(Conta conta) {
		this.conta = conta;
	}

	

	@Override
	public String toString() {
		return "Users [id=" + id + ", nome=" + name + ", sobrenome=" + sobrenome + ", cpf=" + cpf + ", telefone="
				+ telefone + ", cep=" + cep + ", email=" + email + ", password=" + password + ", conta=" + conta + "]";
	}



	public String criptografia(String password) throws Exception {
		
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] messageDigest = md5.digest(password.getBytes());
		
		StringBuilder sb = new StringBuilder();
		
		for (byte b: messageDigest) {
			sb.append(String.format("%02x", b));

		}

		return sb.toString();
		
	}
	
	
}
