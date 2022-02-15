package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.LineaPedido;
import com.example.demo.model.Pedido;
import com.example.demo.repository.LineaPedidoRepository;
import com.example.demo.repository.PedidoRepository;

@Service
public class LineaPedidoService {
	
	@Autowired
	private LineaPedidoRepository repositorio;

}
