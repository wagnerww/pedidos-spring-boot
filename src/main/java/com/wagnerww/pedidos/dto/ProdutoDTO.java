package com.wagnerww.pedidos.dto;

import java.io.Serializable;

public class ProdutoDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String Nome;
	private Double preco;
	
	public ProdutoDTO() {
		
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNome() {
		return Nome;
	}

	public void setNome(String nome) {
		Nome = nome;
	}

	public Double getPreco() {
		return preco;
	}

	public void setPreco(Double preco) {
		this.preco = preco;
	}
	
	
	
}
