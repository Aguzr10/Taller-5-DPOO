package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class PedidoTest {
	private Pedido pedido;
	private Pedido pedido2;

	@BeforeEach
	void setUp() {
		Pedido.resetNumeroPedidos();
		pedido = new Pedido("Juan Perez", "Calle Falsa 123");
		pedido2 = new Pedido("Juan Rodriguez", "Calle Falsa 321");
	}

	@Test
	void testIdPedido() {
		assertEquals(0, pedido.getIdPedido());
		assertEquals(1, pedido2.getIdPedido());
	}

	@Test
	void testNombreCliente() {
		assertEquals("Juan Perez", pedido.getNombreCliente());
		assertEquals("Juan Rodriguez", pedido2.getNombreCliente());
	}

	@Test
	void testAgregarProducto() {
		ProductoMenu producto = new ProductoMenu("Hamburguesa", 10000);
		pedido.agregarProducto(producto);
		assertEquals(11900, pedido.getPrecioTotalPedido());
	}

	@Test
	void testGetPrecioTotalPedido() {
		pedido.agregarProducto(new ProductoMenu("Papas", 5000));
		pedido.agregarProducto(new ProductoMenu("Refresco", 3000));
		int esperado = (int) ((5000 + 3000) * 1.19);
		assertEquals(esperado, pedido.getPrecioTotalPedido());
	}

	@Test
	void testGetPrecioNetoPedido() {
		pedido.agregarProducto(new ProductoMenu("Pizza", 8000));
		pedido.agregarProducto(new ProductoMenu("Jugo", 4000));
		int esperadoNeto = 12000;
		assertEquals(esperadoNeto + (int)(esperadoNeto * 0.19), pedido.getPrecioTotalPedido());
	}

	@Test
	void testGetPrecioIVAPedido() {
		pedido.agregarProducto(new ProductoMenu("Combo", 10000));
		int precioTotal = pedido.getPrecioTotalPedido();
		int esperadoIVA = (int)(10000 * 0.19);
		assertEquals(esperadoIVA, precioTotal - 10000);
	}

	@Test
	void testGenerarTextoFactura() {
		pedido.agregarProducto(new ProductoMenu("Ensalada", 6000));
		String factura = pedido.generarTextoFactura();
		assertTrue(factura.contains("Cliente: Juan Perez"));
		assertTrue(factura.contains("Dirección: Calle Falsa 123"));
		assertTrue(factura.contains("Ensalada"));
		assertTrue(factura.contains("Precio Neto"));
		assertTrue(factura.contains("IVA"));
		assertTrue(factura.contains("Precio Total"));
	}

	@Test
	void guardarFactura() throws FileNotFoundException {
		pedido.agregarProducto(new ProductoMenu("Agua", 2000));
		File archivo = new File("factura_test.txt");
		pedido.guardarFactura(archivo);
		assertTrue(archivo.exists());

		Scanner scanner = new Scanner(archivo);
		StringBuilder contenido = new StringBuilder();
		while (scanner.hasNextLine()) {
			contenido.append(scanner.nextLine()).append("\n");
		}
		scanner.close();

		String texto = contenido.toString();
		assertTrue(texto.contains("Cliente: Juan Perez"));
		assertTrue(texto.contains("Agua"));
		assertTrue(texto.contains("2000"));
		archivo.delete();
	}
}
