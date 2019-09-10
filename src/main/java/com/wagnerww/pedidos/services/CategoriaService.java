package com.wagnerww.pedidos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wagnerww.pedidos.domain.Categoria;
import com.wagnerww.pedidos.repositories.CategoriaRepository;

import javassist.tools.rmi.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired //Essa dependencia é automáticamente injetada pelo spring
	private CategoriaRepository repo;
	
	public Categoria buscar(Integer id){
		Optional<Categoria> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Categoria.class.getName()));
		//}
	//	return obj;
	}
	
}
