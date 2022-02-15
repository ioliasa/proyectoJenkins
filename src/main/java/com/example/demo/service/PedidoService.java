package com.example.demo.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.LineaPedido;
import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.LineaPedidoRepository;
import com.example.demo.repository.PedidoRepository;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;


@Service
public class PedidoService {
	/**
	 * Coleccion de pedidos
	 * get by idPedido
	 * getAll
	 * addProducto
	 * remove producto
	 */
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PedidoRepository repositorioPedido;
	
	@Autowired
	private LineaPedidoRepository repositorioLineaPedido;
	
	@Autowired
	private UsuarioRepository repositorioUsuario;
	
	private Pedido nuevoPedido = new Pedido();
	
	@PostConstruct
	public void init() {
		
	}
	
	/*
	 * lista de productos en carrito, aqui se añadiran los productos que se vayan cliclando en
	 * el catálogo
	 */
	private HashSet<Producto> productosEnCarrito = new HashSet<Producto>();
	
	
	/**
	 * Con este metodo añadiremos los productos que se cliclen en el catálogo en una lista 
	 * a la que llamamos productos en carrito, para ello le pasamos un nuevo producto al metodo
	 * @param Le pasamos un nuevo producto
	 */
	public void addProducto (Producto productoNuevo) {
		for(Producto producto: productoService.findAll()) {
			//Si el productoId es igual al getId lo que haremos será aumentar la variable cantidad de producto y ademas lo añadiremos al array productosEnCarrito
			if(producto.getId()==(productoNuevo.getId()) ) {
				for(Producto productoCarrito: productosEnCarrito) {
					//Si el producto es igual al producto en productoCarrito lo que haremos será aumentar la cantidad de productoCarrito
					if(producto.equals(productoCarrito)) {
						productoCarrito.setCantidad(productoCarrito.getCantidad()+1);
					}
				}
				if(!productosEnCarrito.contains(producto)) {
					//Si no ha encontrado el producto en la lista productoCarrito es porque es la primera vez ue se añade, por lo que modifico la cantidad en la lista productos
					//IMPORTANTE si no le indico que es 1 no va a funcionar bien si borro y vuelvo a añadir el producto
					producto.setCantidad(1);
					productosEnCarrito.add(producto);
				}
				
			}
		}
	}
	
	//Devolvemos los productos que estan en el carrito
	public HashSet<Producto> mostrarProductosEnCarrito(){
		return productosEnCarrito;
	}
	
	/**
	 * Metodo para eliminar un producto del carrito, para ello le pasamos un producto a borrar y
	 * lo eliminará de la lista de productos en carrito, ya que los productos van identificado por
	 *  el id
	 * @param Le pasamos el producto que vamos a borrar
	 */
	public void borrarProducto (Producto productoBorrar) {
		productosEnCarrito.remove(productoBorrar);
		
	}
	
	//Modificamos las cantidades de los productos que vienen del carrito
	//Modicamos la lista de productos en carrito
	/**
	 * Este método es para asignar la cantidad de producto que hemos añadido y se lo asignaremos
	 * al atributo cantidad de los productos, para ello le pasamos un parámetro cantidad que lo recogemos
	 * en la pagina de carrito
	 * @param Le pasamos las cantidades
	 */
	public void modificarCantidades (Integer[] cantidades) {
		int contador=0;
		for (Producto producto: productosEnCarrito ) {
			producto.setCantidad(cantidades[contador]);
			contador++;
		}
	}
	
	//Creamos un nuevo pedido
//	Creamos una constante para la referencia de los pedidos
	/**
	 * Este metodo es para crear nuevo pedido, para ello le pasamos el metodo de pago, el coste de envio,
	 * la direccion, el telefono y un email, con esto y con la lista de productos en carrito 
	 * construimos un nuevo pedido
	 * @param metodoPago
	 * @param costeEnvio
	 * @param direccion
	 * @param telefono
	 * @param email
	 */
	public void crearPedido (String metodoPago, Double costeEnvio, String direccion, String telefono, String email) {
		
		Pedido nuevoPedido1 = new Pedido();
		nuevoPedido1.lineasPedidos.clear();

		for (Producto producto: productosEnCarrito) {
			LineaPedido nuevaLineaPedido = new LineaPedido(nuevoPedido1, producto, producto.getCantidad());
			nuevoPedido1.setLineasPedidos(nuevaLineaPedido);

		}
		nuevoPedido1.setMetodoPago(metodoPago);
		nuevoPedido1.setCosteEnvio(costeEnvio);
		nuevoPedido1.setDireccion(direccion);
		nuevoPedido1.setTelefono(telefono);
		nuevoPedido1.setEmail(email);
		nuevoPedido1.calcularImporte();
		
		nuevoPedido = nuevoPedido1;
		

		
		
		System.out.println(productosEnCarrito + "productos");
	}
	
	
	
	/**
	 * Metodo para mostrar el pedido
	 * @return nuevoPedido
	 */
	public Pedido mostrarPedido() {
		
		return nuevoPedido;
	}
	
	
	/**
	 * Con este metodo borramos los pedidos, en caso que exista el pedido y se pueda borrar
	 * devolverá true y en caso contrario devolvera false
	 * @param pedidoBorrar
	 * @param usuario
	 * @return devuleve true o false segun si puede o no borrar el pedido
	 */
	public void borrarPedido (Pedido pedidoBorrar, Usuario usuario) {
		for(LineaPedido lineaPedidoBorrar: pedidoBorrar.getLineasPedidos()) {
			repositorioLineaPedido.delete(lineaPedidoBorrar);
		}
		repositorioPedido.delete(pedidoBorrar);
		Usuario usuarioBorrarPedido = repositorioUsuario.getById(usuario.getNickName());
		usuarioBorrarPedido.listaPedidos.remove(pedidoBorrar);
		repositorioUsuario.save(usuarioBorrarPedido);

		
	}
	
	/**
	 * Este metodo es para editar pedidos y poder cambiar la cantidad de productos en los pedidos,
	 * para hacer esto le pasamos un usuario y un pedido, el cual lo buscamos y sacamos la lista 
	 * de productos de este pedido, y como le hemos pasado una lista de cantidade ordenada pues cambiamos 
	 * las cantidades de esa lista de productos
	 * @param cantidades
	 * @param pedidoBuscar
	 * @param usuario
	 */
	public void modificarCantidadesEditar (Integer[] cantidades, Pedido pedidoBuscar, String metodoPago, Double costeEnvio, String direccion, String telefono, String email) {
		int contador=0;

		Pedido nuevoPedido = repositorioPedido.getById(pedidoBuscar.getRef());
		for(LineaPedido lineaPedido: nuevoPedido.getLineasPedidos()) {
			lineaPedido.setCantidad(cantidades[contador]);
			contador++;
		}
		
		nuevoPedido.setMetodoPago(metodoPago);
		nuevoPedido.setCosteEnvio(costeEnvio);
		nuevoPedido.setDireccion(direccion);
		nuevoPedido.setTelefono(telefono);
		nuevoPedido.setEmail(email);
		nuevoPedido.calcularImporte();
		repositorioPedido.save(nuevoPedido);
	}
	


	/**
	 * Este metodo cargará en base de datos todo lo referente a un pedido cuando el pedido se haya aceptado
	 * @param idUsuario
	 * @param pedido
	 */
	public void cargarPedidoBD(String idUsuario, Pedido pedido) {
		productosEnCarrito.clear();
		
		//Al meter el pedido ya se meten las lineas de pedido
		repositorioPedido.save(pedido);

		Usuario nuevoUsuario = repositorioUsuario.getById(idUsuario);
		nuevoUsuario.addPedido(pedido);
		repositorioUsuario.save(nuevoUsuario);
		
		
	}
	
	/**
	 * Este metodo recibe el id del usuario del cual vamos a sacar de la base de datos
	 * Una vez sacado devolvemos la lista de pedidos que tiene este usuario
	 * @param idUsuario
	 * @return lista de pedidos
	 */
	public List<Pedido> darListaPedidos(String idUsuario) {
		Usuario nuevoUsuario = repositorioUsuario.getById(idUsuario);
		return nuevoUsuario.getListaPedidos();
	}

	
	/**
	 * Este metodo recibe una referencia de pedido y con esta referencia busca en la base de datos un pedido
	 * @param refPedido
	 * @return pedido que se corresponde con la ref
	 */
	public Pedido mostrarPedidoDB(int refPedido) {
		
		return repositorioPedido.getById(refPedido);
	}

	

}
