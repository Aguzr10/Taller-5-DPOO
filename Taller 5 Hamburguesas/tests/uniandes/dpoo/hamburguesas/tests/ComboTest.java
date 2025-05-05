package uniandes.dpoo.hamburguesas.tests;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import uniandes.dpoo.hamburguesas.mundo.*;

class ComboTest {

    @Test
    void testGetNombre() {
        ArrayList<ProductoMenu> items = new ArrayList<>();
        Combo combo = new Combo("Combo Delicioso", 0.9, items);

        assertEquals("Combo Delicioso", combo.getNombre());
    }

    @Test
    void testGetPrecioConDescuento() {
        ProductoMenu p1 = new ProductoMenu("Hamburguesa", 10000);
        ProductoMenu p2 = new ProductoMenu("Papas", 5000);
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(p1);
        items.add(p2);

        Combo combo = new Combo("Combo Ahorro", 0.9, items);

        assertEquals(13500, combo.getPrecio());
    }

    @Test
    void testGenerarTextoFactura() {
        ProductoMenu p1 = new ProductoMenu("Hamburguesa", 10000);
        ProductoMenu p2 = new ProductoMenu("Papas", 5000);
        ArrayList<ProductoMenu> items = new ArrayList<>();
        items.add(p1);
        items.add(p2);

        Combo combo = new Combo("Combo Factura", 0.8, items);

        String texto = combo.generarTextoFactura();

        assertTrue(texto.contains("Combo Combo Factura"));
        assertTrue(texto.contains("Descuento: 0.8"));
        assertTrue(texto.contains("12000"));
    }
}