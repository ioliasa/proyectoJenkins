package com.example.demo.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
public class LineaPedido {
	

	
	@ManyToOne(cascade = CascadeType.ALL)
	private Pedido pedido;
	

	
	@ManyToOne(fetch= FetchType.EAGER)
	private Producto producto;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	
	private int cantidad;
	
	private double importeTotal;
	
	
	/**
	 * Este metodo calculara el importe total de la linea de pedido,
	 * para ello cogerá la cantidad de producto que hay y lo multiplicará por el precio del producto
	 * @return devolverá un double con el importe total
	 */
	public double getImporteTotal() {
		 this.importeTotal = this.cantidad * this.producto.getPrecio();
		
		 importeTotal = Math.round(importeTotal * Math.pow(10, 2)) / Math.pow(10, 2);
		return importeTotal;
	}
	public void setImporteTotal(double importeTotal) {
		this.importeTotal = importeTotal;
	}
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	public Producto getProducto() {
		return producto;
	}
	public void setProducto(Producto producto) {
		this.producto = producto;
	}
	public int getCantidad() {
		return cantidad;
	}
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	public LineaPedido() {
		
	}
	
	public LineaPedido(Pedido pedido, Producto producto, int cantidad) {
		this.pedido = pedido;
		this.producto = producto;
		this.cantidad = cantidad;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(pedido, producto);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LineaPedido other = (LineaPedido) obj;
		return Objects.equals(pedido, other.pedido) && Objects.equals(producto, other.producto);
	}
	
	
	
	

}
