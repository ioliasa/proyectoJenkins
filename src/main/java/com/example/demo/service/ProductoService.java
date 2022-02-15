package com.example.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepository;
import com.example.demo.repository.UsuarioRepository;

/**
 * Para hacer este servicio vamos a implementar la interfaz de producto, para así tener varias formas 
 * de hacerlo con respecto a los otros servicios
 * @author javier
 *
 */
@Primary
@Service  
public class ProductoService implements ProductoServiceInterface{
	/**
	 * Coleccion de productos
	 * get by id
	 * get all
	 */

	@Autowired
	private ProductoRepository repositorio;
	
	private ArrayList<Producto> productos = new ArrayList<Producto>();
	
	@PostConstruct
	public void init() {
		
		/**
		 * De esta forma cremaos todos los productos y los almacenamos en la bd
		 */
				repositorio.save(new Producto("NJSJ Altavoces PC", "Altavoz 10W ALTAVOZ 2.0 USB GAMING sobremesa, con luces led que cambian de color ajustable", 20.5, "/imagenes/altavoz.jpg", 0));
				repositorio.save(new Producto("HyperX HX-HSCF Cascos", "Cascos Gaming inalámbricos, sonido 5.1 envolvente, 100% Gaming, sonido perfecto para juegos", 104.13, "/imagenes/cascos.jpg", 0));
				repositorio.save(new Producto("Exco-Alfombrilla Gaming", "Alfombrilla antidezlizante de goma, gruesa, perfecto movimiento del raton, sugeción articular", 11.9, "/imagenes/alfombrilla.jpg", 0));
				repositorio.save(new Producto("Mesa Gaming MGD", "Mesa ergonómica Gaming fabricada en fibra de carbono, patas de aluminio, maxima resistencia", 114.56, "/imagenes/mesa.jpg", 0));
				repositorio.save(new Producto("MSI Teclado Gaming", "Teclado mecánico Gaming RGB, máxima rapidez, tacto inmejorable, dureza ajustable colores cambiantes", 28.92, "/imagenes/teclado.jpg", 0));
				repositorio.save(new Producto("MSI Optix MAG30", "Monitor Plano Gaming 29.5 pulgadas 200HZ, máxima calidad, distintos puertos de entrada, brillo ajustable", 446.10, "/imagenes/pantalla.jpg", 0));
				repositorio.save(new Producto("MSI Optix MAG30", "Monitor Plano Gaming 29.5 pulgadas 200HZ, máxima calidad, distintos puertos de entrada, brillo ajustable", 446.10, "/imagenes/pantalla.jpg", 0));
	}
	
	/**
	 * Lista para devolver todos los productos
	 * @return lista de prodcutos
	 */
	public List<Producto> findAll(){
		return repositorio.findAll();
	}

	@Override
	public Producto add(Producto p) {
		// TODO Auto-generated method stub
		return repositorio.save(p);
	}

	@Override
	public Producto findById(int id) {
		// TODO Auto-generated method stub
		return repositorio.findById((long) id).orElse(null);
	}

	@Override
	public Producto edit(Producto p) {
		// TODO Auto-generated method stub
		return repositorio.save(p);
	}
	
	
	
}
