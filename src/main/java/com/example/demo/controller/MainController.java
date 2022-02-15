package com.example.demo.controller;




import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Pedido;
import com.example.demo.model.Producto;
import com.example.demo.model.Usuario;
import com.example.demo.repository.UsuarioRepository;
import com.example.demo.service.LineaPedidoService;
import com.example.demo.service.PedidoService;
import com.example.demo.service.ProductoService;
import com.example.demo.service.UsuarioService;

@Controller 
public class MainController {


	
	
	@Autowired
	private HttpSession sesion;
	
	@Autowired
	private ProductoService productoService;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PedidoService pedidoService;
	
	@Autowired
	private LineaPedidoService lineaPedidoService;
	
	@Autowired
	private UsuarioRepository repositorioUsuario;
	
	
//	###### IR AL INICIO DESDE LOCALHOST
	@GetMapping("/")
	public String irInicio(Model model) {
		return "redirect:/inicio";
	}
	
	
	
//	###### IR AL INICIO
	@GetMapping("/inicio")
	public String listaUsuario(Model model) {
	
		if(sesion.getAttribute("nuevoUsuario")!= null && sesion.getAttribute("nuevoUsuario").equals("disponible")) {
			model.addAttribute("nuevoUsuario", "disponible");
		}
		if(sesion.getAttribute("nuevoUsuario")!= null && sesion.getAttribute("nuevoUsuario").equals("noDisponible")) {
			model.addAttribute("nuevoUsuario", "noDisponible");
		}
		if(sesion.getAttribute("nuevoUsuario")!= null && sesion.getAttribute("nuevoUsuario").equals("noDisponible")) {
			model.addAttribute("nuevoUsuario", "");
		}
		if(sesion.getAttribute("nuevoUsuario")!= null && sesion.getAttribute("nuevoUsuario").equals("incorrecto")) {
			model.addAttribute("nuevoUsuario", "incorrecto");
		}
		model.addAttribute("usuario", new Usuario());
		return "inicio";
	}
	
	
//	###### IR AL CATALOGO
	//Llego a este post a traves del boton, y aqui se comprobará que el usuario es correcto
	//Si es correcto se enviará al get de inicio/catalogo
	//Si es incorrecto se devolverá al inicio
	@PostMapping("inicio/catalogo") 
	public String comprobarUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
		
		
		sesion.setAttribute("nuevoUsuario", "");
		
		//Con el nuevo usuario que creamos para hacer la comprobacion comprobamos a traves del metodo del servicio si el usuario existe, en cuyo caso lo mandaremos al catalogo
		if(usuarioService.comprobarUsuario(usuario)) {
//			Creamos un atributo LOGUEADO para ir comprobando si el usuario se encuentra o no Logueado
			sesion.setAttribute("LOGUEADO", true);
			sesion.setAttribute("usuario", usuario.getNickName());
			return "redirect:/inicio/catalogo";
		}
		else {
			sesion.setAttribute("nuevoUsuario", "incorrecto");
			sesion.setAttribute("LOGUEADO", false);
			return "redirect:";
		}
	}
	
	@GetMapping("inicio/catalogo")
	public String catalogo(Model model) {
		
		model.addAttribute("productos", productoService.findAll());
		String respuesta = "inicio";
		if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
			//Si existe la sesion y el usuario esta logueado lo mandamos al catalogo
			respuesta = "catalogo";
		}
		else {
			//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
			respuesta = "redirect:";
		}
		
			return respuesta;
		
	}
	
	//Añadir producto
	
	@GetMapping("inicio/addProducto")
	public String addProducto(Model model) {

			return "redirect:";
		
	}

	
	@PostMapping("inicio/addProducto") 
	public String addProducto(@ModelAttribute("productoAdd") Producto productoAdd) {
	
			pedidoService.addProducto(productoAdd);
			return "redirect:/inicio/catalogo";
		
	}
	
	
	//######## IR AL CARRITO
	@GetMapping("inicio/carrito")
	public String carrito(Model model) {

		model.addAttribute("productosEnCarrito", pedidoService.mostrarProductosEnCarrito());
		model.addAttribute("usuario", usuarioService.darUsuario((String) sesion.getAttribute("usuario"))); 
		String respuesta = "inicio";
		if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
			//Si existe la sesion y el usuario esta logueado lo mandamos al catalogo

			respuesta = "carrito";
		}
		else {
			//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
			respuesta = "redirect:";
		}
		
			return respuesta;
		
	}

	
	@PostMapping("inicio/carrito") 
	public String carrito() {

		return "redirect:/inicio/carrito";
		
	}
	
	
	//Borrar producto del resumen de pedido
	//La id la mandamos al cliclar un enlace, y viaje en la url, por lo que solo necesitamos un get
	@GetMapping("inicio/borrarProducto/{id}")
	public String borrarProducto(@PathVariable(value="id")int idProducto) {

		String respuesta = "inicio";
		if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
			Producto productoBorrar = new Producto(idProducto);
			pedidoService.borrarProducto(productoBorrar);
			respuesta = "redirect:/inicio/carrito/";
		}
		else {
			//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
			respuesta = "redirect:";
		}
		
			return respuesta;
		
	}

	
	
	//######## IR A LA FACTURA
	@GetMapping("inicio/factura")
	public String factura(Model model) {
		

		model.addAttribute("pedido", pedidoService.mostrarPedido());

		model.addAttribute("usuario", usuarioService.darUsuario((String) sesion.getAttribute("usuario")));
		String respuesta = "inicio";
		if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
			//Si existe la sesion y el usuario esta logueado lo mandamos a la factura
//			System.out.println(pedidoService);
			respuesta = "factura";
		}
		else {
			//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
			respuesta = "redirect:";
		}
		
			return respuesta;
		
	}

	
	@PostMapping("inicio/factura") 
	public String factura(
			Model model,
			@RequestParam(name= "costeEnvio") Double costeEnvio,
			@RequestParam(name= "metodoPago") String metodoPago,
			@RequestParam(name= "direccion") String direccion,
			@RequestParam(name= "telefono") String telefono,
			@RequestParam(name= "email") String email,
			@RequestParam(name= "cantidad") Integer[] cantidades
			) {
		
		pedidoService.modificarCantidades(cantidades);
		pedidoService.crearPedido(metodoPago, costeEnvio, direccion, telefono, email);
		
		
		

		return "redirect:/inicio/factura";
		
	}
	
	
	
	//######## AÑADIR PEDIDOS
	@GetMapping("inicio/addPedido")
	public String addPedido(Model model) {
		

//		model.addAttribute("pedido", pedidoService.mostrarPedido());
		String respuesta = "inicio";
		if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
			
//			System.out.println("tamaño lista ControllerAdd 1: " + repositorioUsuario.getById((String) sesion.getAttribute("usuario")).getListaPedidos().size());
			pedidoService.cargarPedidoBD((String) sesion.getAttribute("usuario"), pedidoService.mostrarPedido());
//			System.out.println("tamaño lista ControllerAdd 2: " + repositorioUsuario.getById((String) sesion.getAttribute("usuario")).getListaPedidos().size());

//			
			
			respuesta = "redirect:/inicio/verPedidos/";
		}
		else {
			
			//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
			respuesta = "redirect:";
		}
		
			return respuesta;
		
	}

	
	@PostMapping("inicio/addPedido") 
	public String addPedido() {

		return "redirect:/inicio/addPedido";
		
	}
	
	
	
	//######## VER PEDIDOS
	@GetMapping("inicio/verPedidos")
	public String verPedido(Model model) {
		

		
		String respuesta = "inicio";
		if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
			//Si existe la sesion y el usuario esta logueado lo mandamos a la factura
			 
//			
//			System.out.println("tamaño lista Controller0: " + repositorioUsuario.getById((String) sesion.getAttribute("usuario")).getListaPedidos().size());
			model.addAttribute("listaPedidos", pedidoService.darListaPedidos((String) sesion.getAttribute("usuario")));
//			model.addAttribute("listaPedidos", usuarioService.getPedidos((String) sesion.getAttribute("usuario")));
//			System.out.println("tamaño lista Controller1: " + repositorioUsuario.getById((String) sesion.getAttribute("usuario")).getListaPedidos().size());
			respuesta = "pedidos";
		}
		else {
			
			//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
			respuesta = "redirect:";
		}
		
			return respuesta;
		
	}

	
	@PostMapping("inicio/verPedidos") 
	public String verPedido() {

		return "redirect:/inicio/verPedidos";
		
	}
	
	
	
	//######## BORRAR PEDIDOS
	//Borrar producto del resumen de pedido
		//La id la mandamos al cliclar un enlace, y viaje en la url, por lo que solo necesitamos un get
		@GetMapping("inicio/borrarPedido/{ref}")
		public String borrarPedido(@PathVariable(value="ref")int refPedido) {

			String respuesta = "inicio";
			if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
				Pedido pedidoBorrar = new Pedido(refPedido);
				pedidoService.borrarPedido(pedidoBorrar, usuarioService.darUsuario((String) sesion.getAttribute("usuario")));
				respuesta = "redirect:/inicio/verPedidos/";
			}
			else {
				//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
				respuesta = "redirect:";
			}
			
				return respuesta;
			
		}
		
		
		//######## EDITAR PEDIDO
		@GetMapping("inicio/editarPedido/{ref}")
		public String editarPedido(@PathVariable(value="ref")int refPedido, Model model) {

			
			model.addAttribute("usuario", usuarioService.darUsuario((String) sesion.getAttribute("usuario"))); 
			String respuesta = "inicio";
			if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
				//Si existe la sesion y el usuario esta logueado lo mandamos al catalogo
				Pedido pedidoEditar = new Pedido(refPedido);
//				System.out.println("editarPedido###: "+ usuarioService.darPedido(pedidoEditar, usuarioService.darUsuario((String) sesion.getAttribute("usuario"))));
				usuarioService.darPedido(pedidoEditar, usuarioService.darUsuario((String) sesion.getAttribute("usuario")));
//				System.out.println("editarPedido###: "+ usuarioService.darPedido(pedidoEditar, usuarioService.darUsuario((String) sesion.getAttribute("usuario"))));
				model.addAttribute("pedido", usuarioService.darPedido(pedidoEditar, usuarioService.darUsuario((String) sesion.getAttribute("usuario"))));
				
				respuesta = "editarPedido";
			}
			else {
				//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
				respuesta = "redirect:";
			}
			
				return respuesta;
			
		}
		
		
		//######## IR A LA FACTURA EDITADA
		@GetMapping("inicio/facturaEditada")
		public String facturaEditada() {
			
				return "redirect:";
		}

		
		@PostMapping("inicio/facturaEditada") 
		public String facturaEditada(
				Model model,
				@RequestParam(name= "costeEnvio") Double costeEnvio,
				@RequestParam(name= "metodoPago") String metodoPago,
				@RequestParam(name= "direccion") String direccion,
				@RequestParam(name= "telefono") String telefono,
				@RequestParam(name= "email") String email,
				@RequestParam(name= "cantidad") Integer[] cantidades,
				@RequestParam(name= "ref") int ref
				) {
			


			String respuesta = "inicio";
			if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
				Pedido pedidoBuscar = new Pedido(ref);
				
				//No se muy bien porque debe estar esta linea, pero si se quita deja de funcionar las modificaciones
				usuarioService.darPedido(pedidoBuscar, usuarioService.darUsuario((String) sesion.getAttribute("usuario")));
				pedidoService.modificarCantidadesEditar(cantidades, pedidoBuscar, metodoPago, costeEnvio, direccion, telefono, email);
				
				model.addAttribute("pedido", usuarioService.darPedido(pedidoBuscar, usuarioService.darUsuario((String) sesion.getAttribute("usuario"))));
				model.addAttribute("usuario", usuarioService.darUsuario((String) sesion.getAttribute("usuario")));
				//Si existe la sesion y el usuario esta logueado lo mandamos a la facturaEditada

				respuesta = "facturaEditada";
				
			}
			else {
				//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
				respuesta = "redirect:";
			}
			
				return respuesta;
			
		}
		
		
		
		//######## VER FACTURA
				@GetMapping("inicio/verFactura/{ref}")
				public String verFactura(@PathVariable(value="ref")int refPedido, Model model) {

					
					model.addAttribute("usuario", usuarioService.darUsuario((String) sesion.getAttribute("usuario"))); 
					String respuesta = "inicio";
					if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
						//Si existe la sesion y el usuario esta logueado lo mandamos al catalogo
//						Pedido pedidoEditar = new Pedido(refPedido);
//						usuarioService.darPedido(pedidoEditar, usuarioService.darUsuario((String) sesion.getAttribute("usuario")));
//						model.addAttribute("pedido", usuarioService.darPedido(pedidoEditar, usuarioService.darUsuario((String) sesion.getAttribute("usuario"))));
						model.addAttribute("pedido", pedidoService.mostrarPedidoDB(refPedido));
						respuesta = "verFactura";
					}
					else {
						//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
						respuesta = "redirect:";
					}
					
						return respuesta;
					
				}

				
				
				
				
				//######## CERRAR SESION
				@GetMapping("inicio/logout")
				public String logout() {

					sesion.invalidate();
						return "redirect:";
					
				}
				
				//######## RegistrarUsuario
				@GetMapping("inicio/registro")
				public String registro(Model model) {

					model.addAttribute("usuario", new Usuario());
						return "registro";
					
				}
		
				
//				###### Registrar usuario
			
				@PostMapping("inicio/addUsuario") 
				public String addUsuario(@ModelAttribute("usuario") Usuario usuario, Model model) {
					if(usuarioService.addUsuario(usuario)) {
						
						sesion.setAttribute("nuevoUsuario", "disponible");
					}
					else {
						sesion.setAttribute("nuevoUsuario", "noDisponible");
					}
						
						
						return "redirect:";
					
				}
				
				@GetMapping("inicio/addUsuario")
				public String addUsuario(Model model) {
					
					model.addAttribute("productos", productoService.findAll());
					String respuesta = "inicio";
					if(sesion.getAttribute("LOGUEADO") != null && (boolean) sesion.getAttribute("LOGUEADO")) {
						//Si existe la sesion y el usuario esta logueado lo mandamos al catalogo
						respuesta = "catalogo";
					}
					else {
						//Si el usuario no esta logado lo mandamos al getMapping del inicio, para que se loguee
						respuesta = "redirect:";
					}
					
						return respuesta;
					
				}
		
	
	
	
	
}
