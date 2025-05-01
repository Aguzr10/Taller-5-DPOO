package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ComboTest {

    @Test
    void testConstructorYGetNombre() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(new ProductoMenu("Hamburguesa", 12000));
        items.add(new ProductoMenu("Papas", 8000));
        Combo c = new Combo("Combo prueba", 0.10, items);
        assertEquals("Combo prueba", c.getNombre());
    }

    @Test
    void testGetPrecioConDescuento() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(new ProductoMenu("A", 10000));
        items.add(new ProductoMenu("B", 5000));
        Combo c = new Combo("C", 0.10, items);
        assertEquals(13500, c.getPrecio());
    }

    @Test
    void testGenerarTextoFacturaFormato() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(new ProductoMenu("X", 2000));
        Combo c = new Combo("DescTest", 0.20, items);
        String txt = c.generarTextoFactura();
        assertTrue(txt.startsWith("Combo DescTest\n"));
        assertTrue(txt.contains("Descuento: 0.2"));
        assertTrue(txt.trim().endsWith(String.valueOf(c.getPrecio())));
    }
}
