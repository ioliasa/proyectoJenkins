package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;



@Primary
@Service("UsuarioService")
public class UsuarioService implements UsuarioServiceInterface{
	
	/**
	 * Coleccion de usuarios
	 * get by nickName
	 * login
	 * add pedido
	 * get allPedidos
	 * get pedido by ref
	 * remove pedido
	 */
	
	
	@Autowired
	private UsuarioRepository repositorio;
	
	/**
	 * Lista de todos los usuarios que existen
	 */
	private ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();
		
	/**
	 * Metodo para comprobar que el usuario existe en la lista de usuarios
	 * @param usuario
	 * @return true o false
	 */
	public boolean comprobarUsuario(Usuario usuario) {
		Usuario usuarioComprobar = findById(usuario.getNickName());
		if(usuarioComprobar != null && usuarioComprobar.getPass().equals(usuario.getPass())) {
				return true;
		}
		else {
			return false;
		}

		

	}
	
	/**
	 * Metodo que devuelve un usuario, para ello creamos otro usuario con el mismo identificador
	 * para poder buscarlo en la lista de usuarios
	 * @param object que sera un usuario
	 * @return el usuario que buscamos
	 */
	public Usuario darUsuario(String idUsuario) {
		return findById(idUsuario);
		
	}
	
	
	/**
	 * Metodo para mostrar un pedido concreto, para ello le pasamos un pedido y un usuario
	 * @param pedidoEditar
	 * @param usuario
	 * @return devuleve el pedido que buscamos
	 */
	public Pedido darPedido(Pedido pedidoEditar, Usuario usuario) {
		usuario.setListaPedidos(repositorio.getById(usuario.getNickName()).getListaPedidos());
		System.out.println("#### a ver si hay pedidos del usuario" + usuario.getListaPedidos().size());
		return usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoEditar));
		
	}
	
	
	/**
	 * Metodo para modificar un pedido, para ello le pasamos todos los nuevos parametros, ademas del
	 * un nuevo pedido para poder buscar el pedido que necesitamos y el usuario la que pertenece
	 * @param pedidoBuscar
	 * @param usuario
	 * @param metodoPago
	 * @param costeEnvio
	 * @param direccion
	 * @param telefono
	 * @param email
	 */
	public void modificarPedido (Pedido pedidoBuscar, Usuario usuario, String metodoPago, Double costeEnvio, String direccion, String telefono, String email) {
		
		
		usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoBuscar)).setMetodoPago(metodoPago);
		usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoBuscar)).setCosteEnvio(costeEnvio);
		usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoBuscar)).setDireccion(direccion);
		usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoBuscar)).setTelefono(telefono);
		usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoBuscar)).setEmail(email);
		usuario.listaPedidos.get(usuario.listaPedidos.indexOf(pedidoBuscar)).calcularImporte();
		
		repositorio.save(usuario);
		
	}
	
	
	/**
	 * Metodo para añadir nuevos usuarios
	 * @param newUsuario
	 * @return true o false
	 */
	public boolean addUsuario(Usuario newUsuario) {
		repositorio.save(newUsuario);
		return listaUsuarios.add(newUsuario);
	}
	
//	public void addPedido(Pedido pedidoAdd, usuario)

	@Override
	public Usuario add(Usuario e) {
		return repositorio.save(e);
	}

	@Override
	public List<Usuario> findAll() {
		return repositorio.findAll();
	}

	@Override
	public Usuario findById(String id) {
		return repositorio.findById(id).orElse(null);
	}

	/**
	 * No sirve
	 */
	@Override
	public Usuario edit(Usuario usuario) {
		
		return repositorio.save(usuario);
	}
	
	
	/**
	 * Este metodo devulve una lista de pedidos de un usuario concreto, pasamos el id de usuario para la busqueda
	 * en la base de datos
	 */
	@Override
	public List<Pedido> getPedidos(String usuarioId){
		System.out.println("ME cago en la puta" +repositorio.getById(usuarioId).getListaPedidos().size());
		return repositorio.getById(usuarioId).getListaPedidos();
	}
	
	/**
	 * De esta forma cargamos los usuarios predeterminados en la base de datos
	 */
	@PostConstruct
	public void init() {
		
		repositorio.save(new Usuario("JCampos", "Javier Garcia", "675446757", "Huelva, 23", "holaKAse", "the__campos@hotmail.com"));
		repositorio.save(new Usuario("Antonio", "Antonio Perez", "675446757", "Cadiz 12", "passAntonio","xulitoVacilon@hotmail.com"));
		repositorio.save(new Usuario("user", "", "", "", "user", "xulitoVacilon@hotmail.com"));
		repositorio.save(new Usuario("pepe", "Pepe Campos", "675446757", "Sevilla 22", "pepe", "xulitoVacilon@hotmail.com"));
		
	}


}
