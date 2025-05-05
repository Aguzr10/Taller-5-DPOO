package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;
import uniandes.dpoo.hamburguesas.excepciones.*;

public class RestauranteTest {

    private Restaurante restaurante;

    @BeforeEach
    public void setUp() {
        restaurante = new Restaurante();
    }

    @Test
    public void testIniciarPedido() throws YaHayUnPedidoEnCursoException {
        restaurante.iniciarPedido("Juan", "Calle 123");
        assertNotNull(restaurante.getPedidoEnCurso());
        assertEquals("Juan", restaurante.getPedidoEnCurso().getNombreCliente());
    }

    @Test
    public void testIniciarPedidoConPedidoExistente() {
        assertThrows(YaHayUnPedidoEnCursoException.class, () -> {
            restaurante.iniciarPedido("Juan", "Calle 123");
            restaurante.iniciarPedido("Maria", "Calle 456");
        });
    }

    @Test
    public void testCerrarPedidoSinPedido() {
        assertThrows(NoHayPedidoEnCursoException.class, () -> {
            restaurante.cerrarYGuardarPedido();
        });
    }


    @Test
    public void testCargarIngredientesYMenu() throws Exception {
        File ingredientes = crearArchivoTemporal("Lechuga;100\nTomate;200");
        File menu = crearArchivoTemporal("Hamburguesa;8000");
        File combos = crearArchivoTemporal("Combo1;10%;Hamburguesa");

        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

        assertEquals(2, restaurante.getIngredientes().size());
        assertEquals(1, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());

        ingredientes.delete();
        menu.delete();
        combos.delete();
    }

    @Test
    public void testCargarIngredientesConIngredienteRepetido() throws IOException {
        File ingredientes = crearArchivoTemporal("Lechuga;100\nTomate;200\nLechuga;100");

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, null, null);
        });

        ingredientes.delete();
    }

    @Test
    public void testCargarMenuConProductoRepetido() throws IOException {
        File ingredientes = crearArchivoTemporal(""); 
        File combos = crearArchivoTemporal("");
        File menu = crearArchivoTemporal("Hamburguesa;8000\nHamburguesa;8000");

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });

        menu.delete();
        ingredientes.delete();
        combos.delete();
    }



    private File crearArchivoTemporal(String contenido) throws IOException {
        File temp = File.createTempFile("temp", ".txt");
        FileWriter writer = new FileWriter(temp);
        writer.write(contenido);
        writer.close();
        return temp;
    }
    @Test
    public void testIngredienteRepetido() throws IOException {
        File ingredientes = crearArchivoTemporal("Lechuga;100\nLechuga;200");
        File menu = crearArchivoTemporal("");
        File combos = crearArchivoTemporal("");

        assertThrows(IngredienteRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });

        ingredientes.delete();
        menu.delete();
        combos.delete();
    }
    @Test
    public void testProductoRepetidoEnMenuBase() throws IOException {
        File ingredientes = crearArchivoTemporal("Lechuga;100");
        File menu = crearArchivoTemporal("Hamburguesa;8000\nHamburguesa;9000");
        File combos = crearArchivoTemporal("");

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });

        ingredientes.delete();
        menu.delete();
        combos.delete();
    }
    @Test
    public void testComboRepetido() throws IOException {
        File ingredientes = crearArchivoTemporal("Lechuga;100");
        File menu = crearArchivoTemporal("Hamburguesa;8000");
        File combos = crearArchivoTemporal("Combo1;10%;Hamburguesa\nCombo1;20%;Hamburguesa");

        assertThrows(ProductoRepetidoException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });

        ingredientes.delete();
        menu.delete();
        combos.delete();
    }
    @Test
    public void testProductoFaltanteEnCombo() throws IOException {
        File ingredientes = crearArchivoTemporal("Lechuga;100");
        File menu = crearArchivoTemporal("Hamburguesa;8000");
        File combos = crearArchivoTemporal("Combo1;10%;Pizza"); 

        assertThrows(ProductoFaltanteException.class, () -> {
            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });

        ingredientes.delete();
        menu.delete();
        combos.delete();
    }

    @Test
    public void testGetPedidos() throws YaHayUnPedidoEnCursoException, NoHayPedidoEnCursoException, IOException {
        Restaurante restaurante = new Restaurante();

        restaurante.iniciarPedido("Juan", "Calle 123");
        restaurante.cerrarYGuardarPedido();

        ArrayList<Pedido> pedidos = restaurante.getPedidos();

        assertNotNull(pedidos);
        assertEquals(1, pedidos.size());
        assertEquals("Juan", pedidos.get(0).getNombreCliente());
    }

    @Test
    public void testCerrarPedidoLimpiaPedidoEnCurso() throws Exception {
        restaurante.iniciarPedido("Luis", "Carrera 45");
        restaurante.cerrarYGuardarPedido();

        assertNull(restaurante.getPedidoEnCurso());
    }

    
    @Test
    public void testCargarSoloIngredientes() throws Exception {
        File ingredientes = crearArchivoTemporal("Lechuga;100\nTomate;150");

        
        restaurante.cargarInformacionRestaurante(ingredientes, null, null);

        assertEquals(2, restaurante.getIngredientes().size());
        ingredientes.delete();
    }

    @Test
    public void testCargarArchivosVacios() throws Exception {
        File ingredientes = crearArchivoTemporal("");
        File menu = crearArchivoTemporal("");
        File combos = crearArchivoTemporal("");

        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

        assertEquals(0, restaurante.getIngredientes().size());
        assertEquals(0, restaurante.getMenuBase().size());
        assertEquals(0, restaurante.getMenuCombos().size());

        ingredientes.delete();
        menu.delete();
        combos.delete();
    }

    @Test
    public void testCerrarPedidoCreaCarpetaFacturaSiNoExiste() throws Exception {
        File carpetaFacturas = new File("./facturas/");
        if (carpetaFacturas.exists()) {
            for (File file : carpetaFacturas.listFiles()) {
                file.delete();
            }
            carpetaFacturas.delete();
        }

        restaurante.iniciarPedido("Ana", "Calle Falsa 123");
        restaurante.cerrarYGuardarPedido();

        assertTrue(carpetaFacturas.exists());
    }

    @Test
    public void testCerrarPedidoGeneraFactura() throws Exception {
        restaurante.iniciarPedido("Carlos", "Avenida 456");
        restaurante.cerrarYGuardarPedido();

        ArrayList<Pedido> pedidos = restaurante.getPedidos();
        assertFalse(pedidos.isEmpty());

        Pedido pedido = pedidos.get(0);
        File factura = new File("./facturas/factura_" + pedido.getIdPedido() + ".txt");

        assertTrue(factura.exists());

       
        factura.delete();
    }

    @Test
    public void testGettersIniciales() {
        assertNotNull(restaurante.getIngredientes());
        assertNotNull(restaurante.getMenuBase());
        assertNotNull(restaurante.getMenuCombos());
    }

    

}