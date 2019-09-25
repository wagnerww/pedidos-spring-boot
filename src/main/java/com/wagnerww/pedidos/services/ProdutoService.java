package com.wagnerww.pedidos.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.wagnerww.pedidos.domain.Categoria;
import com.wagnerww.pedidos.domain.Produto;
import com.wagnerww.pedidos.repositories.CategoriaRepository;
import com.wagnerww.pedidos.repositories.ProdutoRepository;

import javassist.tools.rmi.ObjectNotFoundException;



@Service
public class ProdutoService {
	
	@Autowired //Essa dependencia é automáticamente injetada pelo spring
	private ProdutoRepository repo;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto buscar(Integer id){
		Optional<Produto> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Produto.class.getName()));
		//}
	//	return obj;
	}
	
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy); 
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
	
}
