package br.com.costawebs.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.costawebs.cursomc.domain.Categoria;
import br.com.costawebs.cursomc.repositories.CategoriaRepository;
import br.com.costawebs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public Categoria find(Long id) {
		Categoria obj = repository.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName());
		}
		return obj;
	}
	
	public Categoria create(Categoria categoria) {
		categoria.setId(null);
		return repository.save(categoria);
	}
}
