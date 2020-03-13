package br.senai.sp.odonto.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "tb_dentista")
public class Dentista {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull
	@Size(min = 3, max = 100, message = "Por favor digite um nome com o minímo de 3 letras e o máximo de 10 letras.")
	private String nome;
	
	private String cro;
	
	private String telefone;
	
	private String email;
	
	@ManyToMany
	private List<Especialidade> especialidades;
	

	public Long getCodigo() {
		return id;
	}

	public void setCodigo(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCro() {
		return cro;
	}

	public void setCro(String cro) {
		this.cro = cro;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

	public List<Especialidade> getEspecialidades() {
		return especialidades;
	}

	public void setEspecialidades(List<Especialidade> especialidades) {
		this.especialidades = especialidades;
	}

	@Override
	public String toString() {
		return "Dentista [codigo=" + id + ", nome=" + nome + ", cro=" + cro + ", telefone=" + telefone + ", email="
				+ email + "]";
	}
	

}
