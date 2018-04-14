package br.com.costawebs.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.costawebs.cursomc.domain.Cidade;
import br.com.costawebs.cursomc.domain.Cliente;
import br.com.costawebs.cursomc.domain.Endereco;
import br.com.costawebs.cursomc.domain.enums.TipoCliente;
import br.com.costawebs.cursomc.dto.ClienteDTO;
import br.com.costawebs.cursomc.dto.ClienteNewDTO;
import br.com.costawebs.cursomc.repositories.CidadeRepository;
import br.com.costawebs.cursomc.repositories.ClienteRepository;
import br.com.costawebs.cursomc.repositories.EnderecoRepository;
import br.com.costawebs.cursomc.services.exceptions.DataIntegrityException;
import br.com.costawebs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;

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

	@Transactional
	public Cliente create(Cliente cliente) {
		cliente.setId(null);
		cliente = repository.save(cliente);
		enderecoRepository.save(cliente.getEnderecos());
		return cliente;
	}

	public Cliente fromDTO(ClienteDTO clienteDTO) {
		// throw new UnsupportedOperationException();
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
	}

	public Cliente fromDTO(ClienteNewDTO clienteNewDTO) {
		Cliente cliente = new Cliente(null, clienteNewDTO.getNome(), clienteNewDTO.getEmail(),
				clienteNewDTO.getCpfOuCnpj(), TipoCliente.toEnum(clienteNewDTO.getTipo()));

		Cidade cidade = cidadeRepository.findOne(clienteNewDTO.getCidadeId());

		Endereco endereco = new Endereco(null, clienteNewDTO.getLogradouro(), clienteNewDTO.getNumero(),
				clienteNewDTO.getComplemento(), clienteNewDTO.getBairro(), clienteNewDTO.getCep(), cliente, cidade);

		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNewDTO.getTelefone1());
		if (clienteNewDTO.getTelefone2() != null)
			cliente.getTelefones().add(clienteNewDTO.getTelefone2());
		if (clienteNewDTO.getTelefone3() != null)
			cliente.getTelefones().add(clienteNewDTO.getTelefone3());

		return cliente;
	}

	private void updateData(Cliente newObj, Cliente cliente) {
		newObj.setNome(cliente.getNome());
		newObj.setEmail(cliente.getEmail());
	}

}
