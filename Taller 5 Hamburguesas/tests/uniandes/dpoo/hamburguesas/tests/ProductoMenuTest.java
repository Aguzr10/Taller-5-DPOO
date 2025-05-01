package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ProductoMenuTest {

    @Test
    void testConstructorYGetters() {
        ProductoMenu p = new ProductoMenu("Papas medianas", 8000);
        assertEquals("Papas medianas", p.getNombre());
        assertEquals(8000, p.getPrecio());
    }

    @Test
    void testGenerarTextoFactura() {
        ProductoMenu p = new ProductoMenu("Papas medianas", 8000);
        String esperado = "Papas medianas\n            8000\n";
        assertEquals(esperado, p.generarTextoFactura());
    }
}
