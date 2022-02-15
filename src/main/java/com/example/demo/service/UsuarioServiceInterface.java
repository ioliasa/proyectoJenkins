package com.example.demo.service;

import java.util.List;

import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;

public interface UsuarioServiceInterface {
	public Usuario add(Usuario e);
	public List<Usuario>findAll();
	public Usuario findById(String id);
	public Usuario edit(Usuario e);
	public List<Pedido> getPedidos(String usuarioId);


}
