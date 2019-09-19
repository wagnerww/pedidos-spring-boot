package com.wagnerww.pedidos.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.wagnerww.pedidos.domain.Categoria;
import com.wagnerww.pedidos.domain.Cidade;
import com.wagnerww.pedidos.domain.Cliente;
import com.wagnerww.pedidos.domain.Endereco;
import com.wagnerww.pedidos.domain.enums.TipoCliente;
import com.wagnerww.pedidos.domain.Cliente;
import com.wagnerww.pedidos.dto.ClienteDTO;
import com.wagnerww.pedidos.dto.ClienteNewDTO;
import com.wagnerww.pedidos.repositories.CidadeRepository;
import com.wagnerww.pedidos.repositories.ClienteRepository;
import com.wagnerww.pedidos.repositories.EnderecoRepository;
import com.wagnerww.pedidos.services.exceptions.DataIntegrityException;

import javassist.tools.rmi.ObjectNotFoundException;



@Service
public class ClienteService {
	
	@Autowired //Essa dependencia é automáticamente injetada pelo spring
	private ClienteRepository repo;	
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	
	public Cliente buscar(Integer id){
		Optional<Cliente> obj = repo.findById(id);
		//if(obj == null) {
			return obj.orElseThrow(() ->  new com.wagnerww.pedidos.services.exceptions.ObjectNotFoundException("Objeto não encontrado! Id:"+id
					+", Tipo: "+Cliente.class.getName()));
		//}
	//	return obj;
	}
	
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		Cliente newObj = buscar(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		buscar(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que  possui produtos!");
		}
		 
	}
	
	public List<Cliente> findAll(){
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageRequest = new PageRequest(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repo.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null);
	}
	
	public Cliente fromDTO(ClienteNewDTO objDto) {
		Cliente cli = new Cliente( null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), TipoCliente.toEnum( objDto.getTipo()));
		Cidade cid = new Cidade(objDto.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), objDto.getBairro(), objDto.getCep(),cli, cid); 
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDto.getTelefone1());
		if(objDto.getTelefone2()!=null) {
			cli.getTelefones().add(objDto.getTelefone2());
		}
		
		if(objDto.getTelefone3()!=null) {
			cli.getTelefones().add(objDto.getTelefone3());
		}
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	
}
