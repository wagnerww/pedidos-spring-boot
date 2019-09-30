package com.wagnerww.pedidos.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.wagnerww.pedidos.security.UserSS;

public class UserService {

	public static UserSS authenticated() {
		try {
			//Retorna o usuário logdo no sistema
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	
	
}
