package br.senai.sp.odonto.repository;

import org.springframework.data.repository.CrudRepository;

import br.senai.sp.odonto.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
}
