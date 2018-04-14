package br.com.costawebs.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.costawebs.cursomc.domain.Categoria;
import br.com.costawebs.cursomc.dto.CategoriaDTO;
import br.com.costawebs.cursomc.services.CategoriaService;

@RestController
@RequestMapping(value="/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll() {
		List<Categoria> list = service.findAll();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);		
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Long id) {
		Categoria obj = service.find(id);	
		return ResponseEntity.ok().body(obj);		
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Categoria categoria) {
		categoria = service.create(categoria);	
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(categoria.getId()).toUri();
		
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value="/{id}")
	public ResponseEntity<?> update(@RequestBody Categoria categoria, @PathVariable Long id) {
		categoria.setId(id);
		categoria = service.update(categoria);	
		return ResponseEntity.noContent().build();	
	}
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<?> delete( @PathVariable Long id) {
		service.delete(id);	
		return ResponseEntity.noContent().build();	
	}

}
