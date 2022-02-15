package com.example.demo.service;

import java.util.List;


import com.example.demo.model.Producto;


public interface ProductoServiceInterface {
	public Producto add(Producto p);
	public List<Producto>findAll();
	public Producto findById(int id);
	public Producto edit(Producto p);

}
