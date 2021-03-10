package sysadm.api.rest.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sysadm.api.rest.model.Alunos;
import sysadm.api.rest.repository.AlunosRepository;

@RestController
@RequestMapping(value = "/aluno")
public class AlunosController {

	@Autowired
	private AlunosRepository alunosRepository;

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<Alunos> init(@PathVariable(value = "id") Long id) {

		Optional<Alunos> aluno = alunosRepository.findById(id);

		return new ResponseEntity<Alunos>(aluno.get(), HttpStatus.OK);
	}

	@GetMapping(value = "/", produces = "application/json")
	@CacheEvict(value = "listalunos", allEntries = true)
	@CachePut("listalunos")
	public ResponseEntity<List<Alunos>> init() {

		List<Alunos> list = (List<Alunos>) alunosRepository.findAll();

		return new ResponseEntity<List<Alunos>>(list, HttpStatus.OK);
	}
	
	@GetMapping(value = "/alunoPorNome/{nome}", produces = "application/json")
	@CachePut("listalunosnome")
	public ResponseEntity<List<Alunos>> alunoPorNome(@PathVariable("nome") String nome) throws InterruptedException {

		List<Alunos> list = (List<Alunos>) alunosRepository.findAlunoByNome(nome);

		return new ResponseEntity<List<Alunos>>(list, HttpStatus.OK);
	}
	

	@PostMapping(value = "/", produces = "application/json")
	public ResponseEntity<Alunos> cadastrar(@RequestBody Alunos alunos) {

		Alunos alunoSalvo = alunosRepository.save(alunos);

		return new ResponseEntity<Alunos>(alunoSalvo, HttpStatus.OK);
	}

	@PutMapping(value = "/", produces = "application/json")
	public ResponseEntity<Alunos> atualizar(@RequestBody Alunos alunos) {

		// Optional<Alunos> alunotemp = alunosRepository.findById(alunos.getId());

		Alunos alunoSalvo = alunosRepository.save(alunos);

		return new ResponseEntity<Alunos>(alunoSalvo, HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}", produces = "application/text")
	public String delete(@PathVariable("id") Long id) {

		alunosRepository.deleteById(id);

		return "ok";
	}
}
