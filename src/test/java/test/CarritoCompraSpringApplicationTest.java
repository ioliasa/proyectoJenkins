package test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import com.example.demo.model.Pedido;
import com.example.demo.model.Usuario;
import com.example.demo.service.PedidoService;
import com.example.demo.service.UsuarioService;

@ContextConfiguration
@SpringBootTest(classes = com.example.demo.CarritoCompraSpringApplication.class)
class CarritoCompraSpringApplicationTest {

	
	
	
	
	@Test
	void prueba() {
		Usuario usuario = new Usuario("Javier", "123456");
		assertTrue( usuario.getNickName().equals("Javier"));
		;
		
	}

}
