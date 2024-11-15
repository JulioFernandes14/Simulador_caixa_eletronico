package br.com.si.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.si.model.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users,Integer> {

	public Users findByEmail(String email);
	
}
