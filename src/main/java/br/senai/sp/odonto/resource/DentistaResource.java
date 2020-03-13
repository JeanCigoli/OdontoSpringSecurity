package br.senai.sp.odonto.resource;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.senai.sp.odonto.dto.DentistaDTO;
import br.senai.sp.odonto.model.Dentista;
import br.senai.sp.odonto.repository.DentistaRepository;
import br.senai.sp.odonto.upload.FirebaseStorageService;
import br.senai.sp.odonto.upload.UploadInput;
import br.senai.sp.odonto.upload.UploadOutput;

@RestController
@RequestMapping("/odonto")
@CrossOrigin
public class DentistaResource {
	
	@Autowired
	private DentistaRepository dentistaRepository;
	
	@Autowired
	private FirebaseStorageService firebaseStorageService;
	
	@GetMapping("/dentistas")
	public List<Dentista> getDentistas(){
		
		return dentistaRepository.findAll();
		
	}
	
	@GetMapping("/dentistas/cro/{cro}")
	public List<Dentista> getDentistasByCro(@PathVariable String cro){
	
		return dentistaRepository.findByCro(cro);
		
	}
	
	@GetMapping("/dentistas/name/{nome}")
	public List<Dentista> getDentistasByNomeContaining(@PathVariable String nome){
	
		return dentistaRepository.findByNomeContaining(nome);
		
	}
	
	@GetMapping("/dentistas/{id}")
	public ResponseEntity<Dentista> getDentista(@PathVariable Long id){
		
		
		Optional<Dentista> dentista = dentistaRepository.findById(id);
		
		/*Dentista dentistaOptional = (Dentista) dentista.get();
		
		DentistaDTO dentistaDTO = new DentistaDTO();
		
		dentistaDTO.setNome(dentistaOptional.getNome());
		dentistaDTO.setEmail(dentistaOptional.getEmail());
		dentistaDTO.setTelefone(dentistaOptional.getTelefone());*/
		
		return dentista.isPresent() ? 
				ResponseEntity.ok(dentista.get()) : 
					ResponseEntity.notFound().build();
		
	}
	
	@PostMapping("/dentistas")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Dentista> insertDentistas(@Valid @RequestBody Dentista dentista) {
		
		Dentista dentistaGravado = dentistaRepository.save(dentista);
	
		
		return ResponseEntity.ok(dentistaGravado);
		
	}
	
	@DeleteMapping("/dentistas/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteDentista(@PathVariable Long id) {
		
		dentistaRepository.deleteById(id);
		
	}
	
	@PutMapping("/dentistas/{id}")
	public ResponseEntity<Dentista> insertDentistas(@Valid @RequestBody Dentista dentistaBody, @PathVariable Long id) {
		
		Dentista dentistaBanco = dentistaRepository.findById(id).get();
		
		dentistaBody.setCodigo(id);
		
		/*
		 Serve para substituir o dentista que está vindo do body, pelo dentista do banco menos o id
		*/
		
		if(dentistaBanco != null) {
			BeanUtils.copyProperties(dentistaBody, dentistaBanco, "id");

			dentistaRepository.save(dentistaBanco);
			
			return ResponseEntity.ok(dentistaBanco);
						
		}
		
		return ResponseEntity.notFound().build();
		
	}
	
	@PostMapping("/dentistas/foto")
	public ResponseEntity<UploadOutput> uploadFoto(@RequestBody UploadInput foto) {
		
		String url = firebaseStorageService.upload(foto);
		
		return ResponseEntity.ok(new UploadOutput(url));
		
	}
	
	

}
