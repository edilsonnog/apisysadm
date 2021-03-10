package sysadm.api.rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sysadm.api.rest.model.Alunos;

@Repository
public interface AlunosRepository extends JpaRepository<Alunos, Long> {

	@Query("select a from Alunos a where a.nome like %?1%")
	List<Alunos> findAlunoByNome(String nome);
}
