package br.senai.sp.odonto.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.senai.sp.odonto.model.Dentista;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {
	
	List<Dentista> findByCro(String cro);
	
	@Query(value = "SELECT d FROM Dentista d WHERE d.nome LIKE %:nome%")
	List<Dentista> findByNomeContaining(@Param("nome") String nome);

}
