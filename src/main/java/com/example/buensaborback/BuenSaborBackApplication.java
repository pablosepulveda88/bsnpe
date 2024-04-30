package com.example.buensaborback;

import com.example.buensaborback.domain.entities.*;
import com.example.buensaborback.domain.entities.enums.Estado;
import com.example.buensaborback.domain.entities.enums.FormaPago;
import com.example.buensaborback.domain.entities.enums.TipoEnvio;
import com.example.buensaborback.domain.entities.enums.TipoPromocion;
import com.example.buensaborback.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BuenSaborBackApplication {
// Aca tiene que inyectar todos los repositorios
// Es por ello que deben crear el paquete repositorio

// Ejemplo  @Autowired
//	private ClienteRepository clienteRepmanository;

    private static final Logger logger = LoggerFactory.getLogger(BuenSaborBackApplication.class);

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ArticuloManufacturadoDetalleRepository articuloManufacturadoDetalleRepository;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private ProvinciaRepository provinciaRepository;

    @Autowired
    private LocalidadRepository localidadRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private SucursalRepository sucursalRepository;

    @Autowired
    private DomicilioRepository domicilioRepository;

    @Autowired
    private UnidadMedidaRepository unidadMedidaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ArticuloInsumoRepository articuloInsumoRepository;

    @Autowired
    private ArticuloManufacturadoRepository articuloManufacturadoRepository;

    @Autowired
    private ImagenRepository imagenRepository;

    @Autowired
    private PromocionRepository promocionRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    public static void main(String[] args) {
        SpringApplication.run(BuenSaborBackApplication.class, args);
        logger.info("Estoy activo en el main");
    }

    @Bean
    CommandLineRunner init() {
        return args -> {
            logger.info("----------------ESTOY----FUNCIONANDO---------------------");

            // Crea un nuevo Pais
            Pais pais = Pais.builder()
                    .nombre("Argentina")
                    .build();

            // Crea una nueva Provincia
            Provincia provincia = Provincia.builder()
                    .nombre("Buenos Aires")
                    .pais(pais) // Establece la relación con el Pais
                    .build();

            // Crea una nueva Localidad
            Localidad localidad = Localidad.builder()
                    .nombre("La Plata")
                    .provincia(provincia) // Establece la relación con la Provincia
                    .build();


            localidadRepository.save(localidad); //Guarda Repositorio y como cascada Provincia y Pais


            //----------------------------------------------------------------------------------------


            Domicilio domicilioViamonte = Domicilio.builder()
                    .cp(5509).calle("Viamonte")
                    .numero(500).localidad(localidad)
                    .build();

            Sucursal sucursalChacras = Sucursal.builder()
                    .nombre("En chacras")
                    .horarioApertura(LocalTime.of(17, 0))
                    .horarioCierre(LocalTime.of(23, 0))
                    .domicilio(domicilioViamonte)
                    .build();

            Empresa empresaBrown = Empresa.builder()
                    .nombre("Lo de Brown")
                    .cuil(30503167)
                    .razonSocial("Venta de Alimentos")
                    .build();
            empresaBrown.getSucursales().add(sucursalChacras);

            sucursalChacras.setEmpresa(empresaBrown);
            domicilioViamonte.setSucursal(sucursalChacras);

            empresaRepository.save(empresaBrown); //Guarda empresa y como cascada Sucursal y Domicilio

            //----------------------------------------------------------------------------------------

            UnidadMedida unidadMedidaLitros = UnidadMedida.builder()
                    .denominacion("Litros")
                    .build();
            unidadMedidaRepository.save(unidadMedidaLitros);

            ArticuloInsumo cocaCola = ArticuloInsumo.builder()
                    .denominacion("Coca cola")
                    .unidadMedida(unidadMedidaLitros)
                    .esParaElaborar(false)
                    .stockActual(5)
                    .stockMaximo(50)
                    .precioCompra(50.0)
                    .precioVenta(70.0)
                    .build();

            Imagen imagenCoca = Imagen.builder().url("https://m.media-amazon.com/images/I/51v8nyxSOYL._SL1500_.jpg").build();
            cocaCola.getImagenes().add(imagenCoca);

            Categoria categoriaBebidas = Categoria.builder()
                    .denominacion("Bebidas")
                    .build();

            categoriaBebidas.getArticulos().add(cocaCola);
            categoriaBebidas.getSucursales().add(sucursalChacras);
            sucursalChacras.getCategorias().add(categoriaBebidas);

            //Subcategoria
            Categoria categoriaGaseosas = Categoria.builder()
                    .denominacion("Gaseosas")
                    .build();

            categoriaBebidas.getSubCategorias().add(categoriaGaseosas);
            categoriaGaseosas.getSucursales().add(sucursalChacras);
            sucursalChacras.getCategorias().add(categoriaGaseosas);

            imagenCoca.setArticulo(cocaCola);

            categoriaRepository.save(categoriaBebidas); //Guarda Categoria y como cascada guarda SubCategoria, Articulo, UnidadMedida, Imagen


            // --------------------------------------------------------------

            ArticuloManufacturadoDetalle detalleVasoGaseosa = ArticuloManufacturadoDetalle.builder()
                    .cantidad(1d)
                    .build();

            ArticuloManufacturado vasoGaseosa = ArticuloManufacturado.builder()
                    .denominacion("Vaso de Gaseosa")
                    .descripcion("Un vaso plastico chico de gaseosa")
                    .unidadMedida(unidadMedidaLitros)
                    .precioVenta(100.0)
                    .tiempoEstimadoMinutos(1)
                    .preparacion("Verter gaseosa en vaso")
                    .build();

            vasoGaseosa.getArticuloManufacturadoDetalles().add(detalleVasoGaseosa);
            vasoGaseosa.setCategoria(categoriaGaseosas);

            categoriaBebidas.getArticulos().add(cocaCola);

            detalleVasoGaseosa.setArticuloInsumo(cocaCola);

            Imagen imagenVasoGaseosa = Imagen.builder().url("https://storage.googleapis.com/fitia-api-bucket/media/images/recipe_images/1002846.jpg").build();
            vasoGaseosa.getImagenes().add(imagenVasoGaseosa);

            Promocion promocionDeGaseosa = Promocion.builder().denominacion("Dia de los Gaseosa")
                    .fechaDesde(LocalDate.of(2024, 3, 15))
                    .fechaHasta(LocalDate.of(2024, 3, 17))
                    .horaDesde(LocalTime.of(0, 0))
                    .horaHasta(LocalTime.of(23, 59))
                    .descripcionDescuento("15 de marzo es el día de la gaseosa")
                    .precioPromocional(180d)
                    .tipoPromocion(TipoPromocion.Promocion)
                    .build();
            promocionDeGaseosa.getArticulos().add(vasoGaseosa);
            imagenVasoGaseosa.setArticulo(vasoGaseosa);

            articuloManufacturadoRepository.save(vasoGaseosa); //Guarda ArticuloManufacturado y como cascada guarda ArticuloManufacturadoDetalle, Imagen, Promoción


            sucursalChacras.getCategorias().add(categoriaBebidas);
            sucursalChacras.getPromociones().add(promocionDeGaseosa);

            sucursalRepository.save(sucursalChacras); //Update de sucursal para agregar promoción y categoría bebida



            //----------------------------------------------------------------------------------------

			// agregar usuario
			Usuario usuario1 = Usuario.builder()
                    .username("pepe-honguito75")
                    .auth0Id("iVBORw0KGgoAAAANSUhEUgAAAK0AAACUCAMAAADWBFkUAAABEVBMVEX")
                    .build();

            //----------------------------------------------------------------------------------------

			//Agregar imágen de cliente
			Imagen imagenUsuario = Imagen.builder()
                    .url("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQsa2xSPPay4GD7E3cthBMCcvPMADEjFufUWQ&s")
                    .build();



			//agregar domicilios de cliente
			Domicilio domicilioCliente1 = Domicilio.builder()
                    .calle("Sarmiento")
                    .numero(123)
                    .cp(5507)
                    .localidad(localidad)
                    .build();

            //----------------------------------------------------------------------------------------

            // agregar cliente
            Cliente cliente1 = Cliente.builder()
                    .nombre("Vanina")
                    .email("vani3@gmail.com")
                    .apellido("Luna")
                    .imagen(imagenUsuario)
                    .telefono("2614523698")
                    .usuario(usuario1)
                    .fechaNacimiento(LocalDate.of(1991, 8, 15))
                    .build();

            cliente1.getDomicilios().add(domicilioCliente1);

            imagenUsuario.setCliente(cliente1);

            clienteRepository.save(cliente1); //Guardar cliente y como cascada Guarda Imagen y Usuario

            //----------------------------------------------------------------------------------------

			// agregar factura
			// agrega en cascada
			Factura factura = Factura.builder()
                    .fechaFacturacion(LocalDate.of(2024, 2, 13))
                    .formaPago(FormaPago.MercadoPago)
                    .mpMerchantOrderId(1)
                    .mpPaymentId(1)
                    .mpPaymentType("mercado pago")
                    .mpPreferenceId("0001")
                    .totalVenta(2500d)
                    .build();

            //----------------------------------------------------------------------------------------

			// agregar detalle pedido
			// agrega en cascada
			DetallePedido detallePedido1 = DetallePedido.builder()
                    .articulo(vasoGaseosa)
                    .cantidad(1)
                    .subTotal(130d)
                    .build();
			DetallePedido detallePedido2 = DetallePedido.builder()
                    .articulo(cocaCola)
                    .cantidad(1)
                    .subTotal(70d)
                    .build();

            //----------------------------------------------------------------------------------------

			// agregar pedido
			Pedido pedido = Pedido.builder()
					.domicilio(domicilioCliente1)
					.estado(Estado.Entregado)
					.factura(factura)
					.formaPago(FormaPago.MercadoPago)
					.fechaPedido(LocalDate.of(2024, 4, 18))
					.horaEstimadaFinalizacion(LocalTime.of(12, 30))
					.sucursal(sucursalChacras)
					.tipoEnvio(TipoEnvio.Delivery)
					.total(200d)
					.totalCosto(180d)
					.build();

			pedido.getDetallePedidos().add(detallePedido1);
			pedido.getDetallePedidos().add(detallePedido2);
            detallePedido1.setPedido(pedido);
            detallePedido2.setPedido(pedido);
            factura.setPedido(pedido);

			pedidoRepository.save(pedido); //Guardar pedido

            cliente1.getPedidos().add(pedido);

            clienteRepository.save(cliente1); //Update para agregar el pedido


        };
    }
}







