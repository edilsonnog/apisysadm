package sysadm.api.rest.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import sysadm.api.rest.model.Alunos;

@Repository
public interface AlunosRepository extends JpaRepository<Alunos, Long> {

	@Query("select a from Alunos a where a.nome LIKE %?1%")
	List<Alunos> findAlunoByNome(String nome);

	default Page<Alunos> findAlunoByNamePage(String nome, PageRequest pageRequest){
		
		Alunos alunos = new Alunos();
		alunos.setNome(nome);
		
		/*Configuraando para pesquisar por nome e paginação*/
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Alunos> example = Example.of(alunos, exampleMatcher);
		
		Page<Alunos> retorno = findAll(example, pageRequest);
		
		return retorno;
	}
}
