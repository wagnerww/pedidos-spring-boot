package com.wagnerww.pedidos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerww.pedidos.domain.Pedido;
import com.wagnerww.pedidos.repositories.PedidoRepository;

import javassist.tools.rmi.ObjectNotFoundException;



@Service
public class PedidoService {
	
	@Autowired //Essa dependencia é automáticamente injetada pelo spring
	private PedidoRepository repo;
	
	public Pedido buscar(Integer id){
		Optional<Pedido> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Pedido.class.getName()));
		//}
	//	return obj;
	}
	
}
