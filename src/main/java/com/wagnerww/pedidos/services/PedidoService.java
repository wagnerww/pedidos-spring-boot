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
import com.wagnerww.pedidos.repositories.ClienteRepository;
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
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	@Transactional
	public Pedido buscar(Integer id){
		Optional<Pedido> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Pedido.class.getName()));
		//}
	//	return obj;
	}
	
	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.buscar(obj.getCliente().getId()));
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
			ip.setProduto(produtoService.buscar(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		//emailService.sendOrderConfirmationEmail(obj);
		emailService.sendOrderConfirmationHtmlEmail(obj);
		return obj;
	}
	
}
