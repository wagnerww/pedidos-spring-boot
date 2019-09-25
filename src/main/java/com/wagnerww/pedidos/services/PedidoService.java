package com.wagnerww.pedidos.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wagnerww.pedidos.domain.ItemPedido;
import com.wagnerww.pedidos.domain.PagamentoComBoleto;
import com.wagnerww.pedidos.domain.Pedido;
import com.wagnerww.pedidos.domain.enums.EstadoPagamento;
import com.wagnerww.pedidos.repositories.ItemPedidoRepository;
import com.wagnerww.pedidos.repositories.PagamentoRepository;
import com.wagnerww.pedidos.repositories.PedidoRepository;

import javassist.tools.rmi.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired //Essa dependencia é automáticamente injetada pelo spring
	private PedidoRepository repo;
	
	@Autowired
	private BoletoServices boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Transactional
	public Pedido buscar(Integer id){
		Optional<Pedido> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Pedido.class.getName()));
		//}
	//	return obj;
	}
	
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if(obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		for (ItemPedido ip: obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setPreco(produtoService.buscar(ip.getProduto().getId()).getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		
		return obj;
	}
	
}
