package sysadm.api.rest.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Telefones {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String numero;

	@JsonIgnore
	@org.hibernate.annotations.ForeignKey(name = "alunos_id")
	@ManyToOne(optional = false)
	private Alunos alunos;
	
	@JsonIgnore
	@org.hibernate.annotations.ForeignKey(name = "responsaveis_id")
	@ManyToOne(optional = false)
	private Responsaveis responsaveis;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Alunos getAlunos() {
		return alunos;
	}

	public void setAlunos(Alunos alunos) {
		this.alunos = alunos;
	}

	public Responsaveis getResponsaveis() {
		return responsaveis;
	}

	public void setResponsaveis(Responsaveis responsaveis) {
		this.responsaveis = responsaveis;
	}

	
}
