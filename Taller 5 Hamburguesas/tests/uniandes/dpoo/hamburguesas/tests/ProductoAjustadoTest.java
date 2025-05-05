package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ProductoAjustadoTest {
	private ProductoAjustado producto;
	private ProductoMenu productoBase;
	
	@BeforeEach
	void setUp() throws Exception {
		productoBase = new ProductoMenu("Corral Pollo", 17000);
		producto = new ProductoAjustado(productoBase);
	}
	@Test
	void testGetNombre() {
		assertEquals("Corral Pollo", producto.getNombre());
	}
	@Test
	void testGetPrecio() {
	    assertEquals(17000, producto.getPrecio(), "El precio base sin modificaciones debe ser 17000");

	    Ingrediente queso = new Ingrediente("Queso", 2000);
	    Ingrediente tocineta = new Ingrediente("Tocineta", 3000);
	    producto.agregarIngrediente(queso);
	    producto.agregarIngrediente(tocineta);

	    Ingrediente cebolla = new Ingrediente("Cebolla", 0);
	    producto.eliminarIngrediente(cebolla);
	    int precioEsperado = 17000 + 2000 + 3000;

	    assertEquals(precioEsperado, producto.getPrecio(), "El precio con agregados debe reflejar los costos adicionales");
	}

	@Test 
	void testGenerarTextoFactura() {
	    
	    StringBuffer esperadoSinMods = new StringBuffer();
	    esperadoSinMods.append(productoBase); 
	    esperadoSinMods.append("            " + producto.getPrecio() + "\n");

	    assertEquals(esperadoSinMods.toString(), producto.generarTextoFactura(), "Factura sin modificaciones incorrecta");

	    Ingrediente queso = new Ingrediente("Queso", 2000);
	    Ingrediente tocineta = new Ingrediente("Tocineta", 3000);
	    Ingrediente cebolla = new Ingrediente("Cebolla", 0);

	    producto.agregarIngrediente(queso);
	    producto.agregarIngrediente(tocineta);
	    producto.eliminarIngrediente(cebolla);

	    int precioEsperadoConMods = 17000 + 2000 + 3000;

	    StringBuffer esperadoConMods = new StringBuffer();
	    esperadoConMods.append(productoBase);
	    esperadoConMods.append("    +Queso                2000");
	    esperadoConMods.append("    +Tocineta                3000");
	    esperadoConMods.append("    -Cebolla");
	    esperadoConMods.append("            " + precioEsperadoConMods + "\n");

	    assertEquals(esperadoConMods.toString(), producto.generarTextoFactura(), "Factura con modificaciones incorrecta");
	}

}
