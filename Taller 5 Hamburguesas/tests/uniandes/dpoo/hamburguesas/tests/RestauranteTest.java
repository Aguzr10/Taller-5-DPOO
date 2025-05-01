package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

class RestauranteTest {

    private File ingrFile;
    private File menuFile;
    private File comboFile;
    private Restaurante r;

    @BeforeEach
    void setup(@org.junit.jupiter.api.io.TempDir File tempDir) throws IOException {
        ingrFile  = new File(tempDir, "ing.txt");
        menuFile  = new File(tempDir, "menu.txt");
        comboFile = new File(tempDir, "combos.txt");

        try (BufferedWriter w = new BufferedWriter(new FileWriter(ingrFile))) {
            w.write("Queso;1500\nTomate;500\n");
        }
        try (BufferedWriter w = new BufferedWriter(new FileWriter(menuFile))) {
            w.write("H1;1000\nH2;2000\n");
        }
        try (BufferedWriter w = new BufferedWriter(new FileWriter(comboFile))) {
            w.write("C1;50%;H1;H2\n");
        }

        r = new Restaurante();
    }

    @Test
    void testCargaCorrecta() throws Exception {
        r.cargarInformacionRestaurante(ingrFile, menuFile, comboFile);
        assertEquals(2, r.getIngredientes().size());
        assertEquals(2, r.getMenuBase().size());
        assertEquals(1, r.getMenuCombos().size());
        assertEquals("Queso", r.getIngredientes().get(0).getNombre());
        assertEquals("H2", r.getMenuBase().get(1).getNombre());
        assertEquals("C1", r.getMenuCombos().get(0).getNombre());
    }

    @Test
    void testIniciarYCerrarPedidoFlujo() throws Exception {
        r.iniciarPedido("Ana", "Dir");
        assertNotNull(r.getPedidoEnCurso());
        assertDoesNotThrow(() -> r.cerrarYGuardarPedido());
        assertNull(r.getPedidoEnCurso());
    }

    @Test
    void testCerrarSinPedidoEnCurso() {
        assertThrows(NoHayPedidoEnCursoException.class, () -> r.cerrarYGuardarPedido());
    }

    @Test
    void testIniciarConPedidoYaAbierto() throws Exception {
        r.iniciarPedido("A", "D");
        assertThrows(YaHayUnPedidoEnCursoException.class, () -> r.iniciarPedido("B", "D2"));
    }
}
