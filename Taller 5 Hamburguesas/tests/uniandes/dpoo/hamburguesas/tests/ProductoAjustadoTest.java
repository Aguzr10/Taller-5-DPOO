package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class ProductoAjustadoTest {

    private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tomate;

    @BeforeEach
    void setup() {
        base = new ProductoMenu("Hamburguesa sencilla", 12000);
        ajustado = new ProductoAjustado(base);
        queso = new Ingrediente("Queso extra", 1500);
        tomate = new Ingrediente("Tomate", 500);
    }

    @Test
    void testGetNombreDelegado() {
        assertEquals("Hamburguesa sencilla", ajustado.getNombre());
    }

    @Test
    void testPrecioInicial() {
        assertEquals(12000, ajustado.getPrecio());
    }

    @Test
    void testAgregarIngredienteAumentaPrecio() throws Exception {
        Field f = ProductoAjustado.class.getDeclaredField("agregados");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Ingrediente> agregados = (List<Ingrediente>) f.get(ajustado);
        agregados.add(queso);
        assertEquals(13500, ajustado.getPrecio());
        agregados.add(queso);
        assertEquals(15000, ajustado.getPrecio());
    }

    @Test
    void testQuitarIngredienteNoReducePrecio() throws Exception {
        Field f = ProductoAjustado.class.getDeclaredField("eliminados");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Ingrediente> eliminados = (List<Ingrediente>) f.get(ajustado);
        eliminados.add(tomate);
        assertEquals(12000, ajustado.getPrecio());
    }

    @Test
    void testGenerarTextoFactura() throws Exception {
        Field fa = ProductoAjustado.class.getDeclaredField("agregados");
        fa.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Ingrediente> agregados = (List<Ingrediente>) fa.get(ajustado);
        agregados.add(queso);

        Field fe = ProductoAjustado.class.getDeclaredField("eliminados");
        fe.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Ingrediente> eliminados = (List<Ingrediente>) fe.get(ajustado);
        eliminados.add(tomate);

        String txt = ajustado.generarTextoFactura();
        assertTrue(txt.contains("Hamburguesa sencilla"));
        assertTrue(txt.contains("+Queso extra"));
        assertTrue(txt.contains("1500"));
        assertTrue(txt.contains("-Tomate"));
        assertTrue(txt.trim().endsWith("13500"));
    }
}
