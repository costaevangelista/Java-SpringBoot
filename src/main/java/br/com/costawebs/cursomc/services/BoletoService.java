package br.com.costawebs.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.costawebs.cursomc.domain.PagamentoComBoleto;

@Service
public class BoletoService {

	public void preencherPagamamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(instanteDoPedido);
		calendar.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(calendar.getTime());
	}
}
