package br.com.costawebs.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.costawebs.cursomc.domain.Cliente;
import br.com.costawebs.cursomc.dto.ClienteDTO;
import br.com.costawebs.cursomc.repositories.ClienteRepository;
import br.com.costawebs.cursomc.services.exceptions.DataIntegrityException;
import br.com.costawebs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	public List<Cliente> findAll() {
		return repository.findAll();
	}

	public Cliente find(Long id) {
		Cliente obj = repository.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException(
					"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName());
		}
		return obj;
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newObj = find(cliente.getId());
		updateData(newObj, cliente);
		return repository.save(cliente);
	}

	public void delete(Long id) {
		find(id);
		try {
			repository.delete(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir um cliente porque há entidades relacionadas");
		}

	}

	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderby, String direction) {
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderby);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		//throw new UnsupportedOperationException();
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}
	
	private void updateData(Cliente newObj, Cliente cliente) {
		newObj.setNome(cliente.getNome());
		newObj.setEmail(cliente.getEmail());
	}

}
