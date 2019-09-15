package com.wagnerww.pedidos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerww.pedidos.domain.Cliente;
import com.wagnerww.pedidos.repositories.ClienteRepository;

import javassist.tools.rmi.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired //Essa dependencia é automáticamente injetada pelo spring
	private ClienteRepository repo;
	
	public Cliente buscar(Integer id){
		Optional<Cliente> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Cliente.class.getName()));
		//}
	//	return obj;
	}
	
}
