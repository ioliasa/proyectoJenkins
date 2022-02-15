package com.example.demo.model;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "productos")
public class Producto {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "nombre", nullable = false)
	private String nombre;
	
	@Column(name = "precio", nullable = false)
	private double precio;
	
	@Column(name = "descripcion", nullable = false)
	private String descripcion;
	
	@Column(name = "imagen", nullable = false)
	private String imagen;
	
	
	private int cantidad;
	
	@Column(name = "importeTotal", nullable = false)
	private double importeTotal;
	public Producto() {
		
	}
	
	public Producto( int id) {
		this.id = id;
	}
	
	public Producto(String nombre, String descripcion,  double precio, String imagen, int cantidad) {
		super();
		this.nombre = nombre;
		this.imagen = imagen;
		this.descripcion = descripcion;
		this.precio= precio;
		this.cantidad = cantidad;
	}
	
	public int getCantidad() {
		return cantidad;
	}



	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}



	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}


	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	/**
	 * Metodo get que devuelve el importe total, el cual lo caalcula multiplicando el precio 
	 * por la cantidad
	 * @return
	 */
	public double getImporteTotal() {
		double resultado = this.precio * this.cantidad;
		
		resultado = Math.round(resultado * Math.pow(10, 2)) / Math.pow(10, 2);
		return resultado;
	}

	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	/**
	 * Los productos se indentificarán por el id
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Producto other = (Producto) obj;
		return Objects.equals(id, other.id);
	}

}
