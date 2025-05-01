package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

class PedidoTest {

    @TempDir
    File tempDir;

    @Test
    void testIdIncrementalYDatosCliente() {
        Pedido p1 = new Pedido("Ana", "Calle 1");
        Pedido p2 = new Pedido("Beto", "Calle 2");
        assertEquals("Ana", p1.getNombreCliente());
        assertEquals("Beto", p2.getNombreCliente());
        assertTrue(p2.getIdPedido() > p1.getIdPedido());
    }

    @Test
    void testCalculoPreciosEIVA() throws Exception {
        Pedido pedido = new Pedido("Cliente", "Dir");
        pedido.agregarProducto(new ProductoMenu("P1", 10000));
        pedido.agregarProducto(new ProductoMenu("P2", 5000));

        ProductoAjustado pa = new ProductoAjustado(new ProductoMenu("B", 2000));
        Field f = ProductoAjustado.class.getDeclaredField("agregados");
        f.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<Ingrediente> agregados = (List<Ingrediente>) f.get(pa);
        agregados.add(new Ingrediente("Q", 500));

        pedido.agregarProducto(pa);

        int neto = 10000 + 5000 + 2500;
        int iva = (int)(neto * 0.19);
        int total = neto + iva;

        String factura = pedido.generarTextoFactura();
        assertTrue(factura.contains("Precio Neto:  " + neto));
        assertTrue(factura.contains("IVA:          " + iva));
        assertTrue(factura.contains("Precio Total: " + total));
    }

    @Test
    void testGuardarFacturaEscribeArchivo(@TempDir File tempDir) throws IOException {
        Pedido p = new Pedido("Cli", "Dir");
        p.agregarProducto(new ProductoMenu("Foo", 3000));
        File out = new File(tempDir, "f.txt");
        p.guardarFactura(out);
        String contenido = Files.readString(out.toPath());
        assertEquals(p.generarTextoFactura(), contenido);
    }
}
