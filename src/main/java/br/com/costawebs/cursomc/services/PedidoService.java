package br.com.costawebs.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.costawebs.cursomc.domain.ItemPedido;
import br.com.costawebs.cursomc.domain.PagamentoComBoleto;
import br.com.costawebs.cursomc.domain.Pedido;
import br.com.costawebs.cursomc.domain.enums.EstadoPagamento;
import br.com.costawebs.cursomc.repositories.ItemPedidoRepository;
import br.com.costawebs.cursomc.repositories.PagamentoRepository;
import br.com.costawebs.cursomc.repositories.PedidoRepository;
import br.com.costawebs.cursomc.repositories.ProdutoRepository;
import br.com.costawebs.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repository;

	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	ProdutoRepository produtoRepository;
	
	@Autowired
	ItemPedidoRepository itemPedidoRepository;

	public Pedido find(Long id) {
		Pedido obj = repository.findOne(id);
		if (obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName());
		}
		return obj;
	}

	public Pedido create(Pedido pedido) {
		pedido.setId(null);
		pedido.setInstante(new Date());
		pedido.getPagamento().setEstado(EstadoPagamento.PEDENTE);
		pedido.getPagamento().setPedido(pedido);
		if (pedido.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) pedido.getPagamento();
			boletoService.preencherPagamamentoComBoleto(pagto, pedido.getInstante());
		}
		
		pedido = repository.save(pedido);
		pagamentoRepository.save(pedido.getPagamento());
		for(ItemPedido ip : pedido.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoRepository.findOne(ip.getProduto().getId()).getPreco());
			ip.setPedido(pedido);
		}
		itemPedidoRepository.save(pedido.getItens());
		
		return pedido;

	}
}
