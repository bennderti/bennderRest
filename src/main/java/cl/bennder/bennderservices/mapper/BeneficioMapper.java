package cl.bennder.bennderservices.mapper;

import cl.bennder.bennderservices.model.UsuarioBeneficio;
import cl.bennder.entitybennderwebrest.model.*;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;

import java.util.List;

/**
 * Created by Diego on 10-03-2017.
 */
public interface BeneficioMapper {
    
    /***
     * Método encargado de obtener las sucursales de un proveedor según beneficio
     * @param idBeneficio Identificador de beneficio
     * @return Lista de sucursales de proveedor
     */
    @Select("select sp.id_sucursal as idSucursal,sp.id_direccion as idDireccion, c.nombre||' - '||d.calle ||' ('||coalesce('Nro. ' ||d.numero,'S/N')||')' as nombreSucursal  " +
            "from proveedor.sucursal_proveedor sp inner join proveedor.direccion d on sp.id_direccion=d.id_direccion  " +
            "inner join proveedor.comuna c on c.id_comuna=d.id_comuna  " +
            "where sp.id_proveedor =(select id_proveedor from proveedor.beneficio where id_beneficio = #{idBeneficio}  and habilitado=true) and habilitado=true ")
    public List<SucursalProveedor> getSucursalesProveedorByBeneficio(Integer idBeneficio);
    
    @Select(  " select b.id_beneficio as idBeneficio,b.titulo,p.nombre as nombreProveedor,b.id_proveedor as idProveedor "
            + " from proveedor.beneficio b inner join proveedor.proveedor p on b.id_proveedor=p.id_proveedor " +
              " where b.id_beneficio = #{idBeneficio}")
    public Beneficio getInfoGeneralBeneficio(Integer idBeneficio);
    
    
    @Select("select count(1) from proveedor.sucursal_proveedor where id_sucursal= #{idSucursal} and password_pos = #{password} and id_proveedor =#{idProveedor}")
    public Integer esPasswordSucursalValida(@Param("password") String password,@Param("idSucursal") Integer idSucursal, @Param("idProveedor") Integer idProveedor);
    
    /***
     * Obtiene el título del beneficio para completar asunto cuando se envia correo de beneficio seleccionado
     * @param idBeneficio iddentificador de beneficio
     * @author dyanez
     * @return 
     */
    @Select("select titulo from proveedor.beneficio where id_beneficio=#{idBeneficio}")
    public String getTituloBeneficioAsuntoEnvioCorreo(Integer idBeneficio);
    
    /***
     * Obtiene el stock actual del beneficio seleccionado
     * @author dyanez
     * @param idBeneficio identificador de beneficio
     * @return Stock beneficio
     */
    @Select("select stock from proveedor.beneficio where id_beneficio= #{idBeneficio}")
    public Integer getStockBeneficio(Integer idBeneficio);
    
    /***
     * Obtiene ultima información del usuario sobre  beneficio
     * @param uBeneficio
     * @return 
     */
    @Select("SELECT id_usuario as idUsuario,id_beneficio as idBeneficio,id_accion_beneficio as idAccionBeneficio,"
            + " cantidad,codigo_beneficio as codigoBeneficio  FROM USUARIO_BENEFICIO WHERE id_usuario = #{idUsuario} AND id_beneficio = #{idBeneficio}")
    public UsuarioBeneficio getUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    
    /***
     * Descuenta X cantidad de stock a beneficio seleccionado/obtenido
     * @param uBeneficio informacion de beneficio
     */
    @Update("UPDATE proveedor.BENEFICIO SET STOCK = (SELECT STOCK FROM proveedor.BENEFICIO WHERE ID_BENEFICIO = #{idBeneficio}) - #{cantidad} WHERE ID_BENEFICIO = #{idBeneficio}")
    public void descuentaStockBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Inserta accion de usuario beneficio
     * @param uBeneficio 
     */
    @Insert("INSERT INTO usuario_beneficio( " +
"            id_usuario, id_beneficio, id_accion_beneficio, cantidad, codigo_beneficio,codigo_beneficio_encriptado) " +
"    VALUES (#{idUsuario}, #{idBeneficio}, #{idAccionBeneficio}, #{cantidad}, #{codigoBeneficio},#{codigoBeneficioEncriptado})")
    public void guardarUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    @Update("UPDATE usuario_beneficio " +
    "   SET  codigo_beneficio =#{codigoBeneficio},codigo_beneficio_encriptado =#{codigoBeneficioEncriptado},cantidad=#{cantidad} " +
    " WHERE id_usuario = #{idUsuario} AND id_beneficio = #{idBeneficio}")
    public void actualizaCodigoBeneficioObtenido(UsuarioBeneficio uBeneficio);
    
    /***
     * Actualiza la ultima accion del usuario sobre beneficio en la tabla de negocio
     * @param uBeneficio 
     */
    @Update("UPDATE usuario_beneficio " +
    "   SET  id_accion_beneficio= #{idAccionBeneficio} " +
    " WHERE id_usuario = #{idUsuario} AND id_beneficio = #{idBeneficio}")
    public void actualizaAccionUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    @Update("UPDATE fecha_accion_beneficio " +
    "   SET  FECHA = now(),"
      + "id_vendedor_pos = #{idVendedorPOS, jdbcType = NULL}, "
      + " ID_SUCURSAL_CANJE = #{idSucursalcanje, jdbcType = NULL}" +
    " WHERE id_usuario = #{idUsuario} AND id_beneficio = #{idBeneficio} AND id_accion_beneficio = #{idAccionBeneficio}")
    public void actualizaFechaAccionUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Inseerta fecha asociada a la acccion del usuario sobre beneficio/auditoria
     * @param uBeneficio 
     */
    @Insert("INSERT INTO fecha_accion_beneficio( " +
"            id_usuario, id_beneficio, id_accion_beneficio, fecha,id_vendedor_pos,ID_SUCURSAL_CANJE) " +
"    VALUES (#{idUsuario}, #{idBeneficio}, #{idAccionBeneficio}, now(),#{idVendedorPOS, jdbcType = NULL},#{idSucursalcanje, jdbcType = NULL})")
    public void insertaFechaAccionUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Registra fecha accion de beneficio/auditoria sobre estado de beneficio
     * @param uBeneficio
     * @return 
     */
    @Select("SELECT count(1) FROM fecha_accion_beneficio "
            + " WHERE id_usuario = #{idUsuario} "
            + " AND id_beneficio = #{idBeneficio} "
            + " AND id_accion_beneficio = #{idAccionBeneficio}")
    public Integer getFechaUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    
    
    
    
    //https://www.postgresql.org/docs/8.1/static/functions-datetime.html 
    /***
     * Valida si cliente ya habia obtenido determinado beneficio dentro de fecha de vigencia
     * @param idUsuario Identificador de usuario
     * @param idBeneficio Identificador de beneficio
     * @return 
     */
    @Select("SELECT COUNT(1) FROM proveedor.BENEFICIO B INNER JOIN USUARIO_BENEFICIO UB ON UB.ID_BENEFICIO = B.ID_BENEFICIO " +
            "WHERE current_date <=  B.fecha_expiracion AND UB.ID_BENEFICIO = #{b} AND UB.ID_USUARIO = #{u} AND UB.ID_ACCION_BENEFICIO <> 0")
    public Integer usuarioHaObtenidoCuponbeneficio(@Param("u") Integer idUsuario,@Param("b") Integer idBeneficio);
    
    
    /***
     * Obtiene los beneficios de una categoria seleccionda para el cargador (datos simples)
     * @param idCategoria Identificador de la categoria
     * @return 
     */
    @Select("SELECT ID_BENEFICIO AS idBeneficio,TITULO as titulo FROM proveedor.BENEFICIO WHERE ID_CATEGORIA = #{idCategoria}")
    List<BeneficioCargador> getBeneficiosCargadorByIdCat(Integer idCategoria);

//    @Select(" SELECT b.id_beneficio AS idBeneficio," +
//            " b.id_beneficio," +
//            " b.titulo, " +
//            " b.descripcion," +
//            " b.calificacion," +
//            " tb.id_tipo_beneficio," +
//            " tb.nombre," +
//            " bd.porcentaje_descuento as porcentajeDescuento," +
//            " bp.precio_normal as precioNormal," +
//            " bp.precio_oferta as precioOferta," +
//            " p.nombre as nombreProveedor" +
//            " FROM proveedor.beneficio b" +
//            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
//            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
//            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
//            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
//            " WHERE b.id_categoria = #{idCategoria}" +
//            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
//            " AND b.habilitado = TRUE " +
//            " AND b.stock > 0")
//    @TypeDiscriminator(column = "id_tipo_beneficio",
//            cases = {
//            @Case(value = "1", type = Descuento.class),
//            @Case(value = "2", type = Producto.class)
//
//    })
//    @Results({
//            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
//            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
//            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
//    })
//    List<Beneficio> obtenerBeneficiosPorCategoria(Integer idCategoria);
    
    
    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " b.calificacion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM proveedor.beneficio b" +
            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria = #{idCategoria}" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0 "
            + "OFFSET #{paginador.indicePagina}*#{paginador.cantidadPagina} LIMIT #{paginador.cantidadPagina} ")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
            @Case(value = "1", type = Descuento.class),
            @Case(value = "2", type = Producto.class)

    })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosPorCategoriaPaginados(@Param("idCategoria") Integer idCategoria,@Param("paginador")Paginador paginador);
   
    
    /***
     * Obtiene el total de beneficio por categoria de beneficio
     * @param idCategoria
     * @return 
     */
    @Select(" SELECT COUNT(1) " +
            " FROM proveedor.beneficio b" +
            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria = #{idCategoria}" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0")
    Integer obtenerTotalBeneficiosPorCategoria(Integer idCategoria);
    
    
    

    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " b.calificacion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM proveedor.beneficio b" +
            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria = #{idCategoria}" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND bp.precio_oferta BETWEEN #{precioMin} AND #{precioMax}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatFiltradosPorPrecio(@Param("idCategoria") Integer idCategoria, @Param("precioMin") Integer precioMin, @Param("precioMax") Integer precioMax);

//    @Select(" SELECT b.id_beneficio AS idBeneficio," +
//            " b.id_beneficio," +
//            " b.titulo, " +
//            " b.descripcion," +
//            " b.calificacion," +
//            " tb.id_tipo_beneficio," +
//            " tb.nombre," +
//            " bd.porcentaje_descuento as porcentajeDescuento," +
//            " bp.precio_normal as precioNormal," +
//            " bp.precio_oferta as precioOferta," +
//            " p.nombre as nombreProveedor" +
//            " FROM proveedor.beneficio b" +
//            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
//            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
//            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
//            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
//            " WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre})" +
//            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
//            " AND b.habilitado = TRUE " +
//            " AND b.stock > 0")
//    @TypeDiscriminator(column = "id_tipo_beneficio",
//            cases = {
//                    @Case(value = "1", type = Descuento.class),
//                    @Case(value = "2", type = Producto.class)
//
//            })
//    @Results({
//            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
//            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
//            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
//    })
//    List<Beneficio> obtenerBeneficiosPorCategoriaPadre(Integer idCategoriaPadre);
    
    
    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " b.calificacion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM proveedor.beneficio b" +
            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre})" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0 "
            + "OFFSET #{paginador.indicePagina}*#{paginador.cantidadPagina} LIMIT #{paginador.cantidadPagina} ")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosPorCategoriaPadrePaginados(@Param("idCategoriaPadre")Integer idCategoriaPadre,@Param("paginador")Paginador paginador);
    
    
    /***
     * Obtiene el total de beneficios por categoria padre
     * @param idCategoriaPadre
     * @return 
     */
    @Select("SELECT count(1)" +
            "FROM proveedor.beneficio b " +
            "INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio  " +
            "INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor " +
            "LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio " +
            "LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio " +
            "WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre} )" +
            "AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion  " +
            "AND b.habilitado = TRUE  " +
            "AND b.stock > 0")
    Integer obtenerTotalBeneficiosPorCategoriaPadre(Integer idCategoriaPadre);

    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " b.calificacion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM proveedor.beneficio b" +
            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre})" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND bp.precio_oferta BETWEEN #{precioMin} AND #{precioMax}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatPadreFiltradosPrecio(@Param("idCategoriaPadre") Integer idCategoriaPadre, @Param("precioMin") Integer precioMin, @Param("precioMax") Integer precioMax);


    @Select("SELECT b.id_beneficio AS idBeneficio, " +
            "b.id_beneficio as idBeneficioParaCondiciones, " +
            "b.id_beneficio as idBeneficioParaImagenes, " +
            "b.titulo,  " +
            "b.descripcion, " +
            "b.fecha_inicial as fechaInicial, " +
            "b.fecha_expiracion as fechaExpiracion, " +
            "b.fecha_creacion as fechaCreacion, " +
            "b.stock, " +
            "b.limite_stock as limiteStock, " +
            "b.calificacion, " +
            "b.visitas_general as visitasGeneral, " +
            "tb.id_tipo_beneficio, " +
            "tb.nombre, " +
            "bd.porcentaje_descuento as porcentajeDescuento, " +
            "bp.precio_normal as precioNormal, " +
            "bp.precio_oferta as precioOferta," +
            "p.nombre as nombreProveedor, " +
            "c.nombre as nombreCategoria " +
            "FROM proveedor.beneficio b " +
            "INNER JOIN proveedor.categoria c ON c.id_categoria = b.id_categoria " +
            "INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio  " +
            "INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor " +
            "LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio " +
            "LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio " +
            "WHERE b.id_beneficio = #{idBeneficio}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class),
                    @Case(value = "3", type = Adicional.class, results = {@Result(property = "descripciones",javaType = List.class,column = "idBeneficio",many =  @Many(select = "getAdicionales"))})
            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficioParaImagenes", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficio")),
            @Result(property = "condiciones", column = "idBeneficioParaCondiciones", javaType=List.class, many = @Many(select = "obtenerCondicionesBeneficio"))

    })
    Beneficio obtenerDetalleBeneficio(Integer idBeneficio);
    
    @Select("select gancho from proveedor.beneficio_gancho where id_beneficio = #{idBeneficio}")
    public List<String> getAdicionales(Integer idBeneficio);

    //@Select(" SELECT imagen " +
    @Select(" SELECT path " +
            " FROM proveedor.beneficio_imagen" +
            " WHERE id_beneficio = #{idBeneficio}")
    List<BeneficioImagen> obtenerImagenesBeneficio(Integer idBeneficio);

    @Select(" SELECT condicion " +
            " FROM proveedor.condicion_beneficio" +
            " WHERE id_beneficio = #{idBeneficio}")
    List<String> obtenerCondicionesBeneficio(Integer idBeneficio);


    //@Select(" SELECT imagen " +
    @Select(" SELECT path " +
            " FROM proveedor.beneficio_imagen" +
            " WHERE id_beneficio = #{idBeneficio}" +
            " AND orden in (1,2)")
    List<BeneficioImagen> obtenerImagenesBeneficioPreview(Integer idBeneficio);
    
    /**
     * Obtiene lista de los beneficios con mejor calificaciòn, basado en las preferencias de categoría del usuario, con un limite de 9 items.
     * Finalmente si la cantidad de beneficios es menor a 9, lo completa con los beneficios generales con mejor calificación
     * @param idUsuario
     * @return 
     */
    @Select("(SELECT "
                + "B.ID_BENEFICIO AS idBeneficio, "
                + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
                + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
                + "B.TITULO, "
                + "B.CALIFICACION, "
                + "B.ID_TIPO_BENEFICIO, "
                + "BP.PRECIO_NORMAL AS precioNormal, "
                + "BP.PRECIO_OFERTA AS precioOferta, "
                + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
                + "BG.GANCHO, "
                + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN INTERES_USUARIO IU ON B.ID_CATEGORIA = IU.ID_CATEGORIA AND IU.ID_USUARIO = #{idUsuario} " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION AND B.HABILITADO = TRUE AND B.STOCK > 0 " +
            "ORDER BY B.CALIFICACION DESC, B.FECHA_EXPIRACION)" +
            "UNION" +
            "(SELECT "
                + "B.ID_BENEFICIO AS idBeneficio, "
                + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
                + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
                + "B.TITULO, "
                + "B.CALIFICACION, "
                + "B.ID_TIPO_BENEFICIO, "
                + "BP.PRECIO_NORMAL AS precioNormal, "
                + "BP.PRECIO_OFERTA AS precioOferta, "
                + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
                + "BG.GANCHO, "
                + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION AND B.HABILITADO = TRUE AND B.STOCK > 0 " +
            "ORDER BY B.CALIFICACION DESC, B.FECHA_EXPIRACION)" +
            "LIMIT 9")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficioParaImagenes", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
            @Result(property = "condiciones", column = "idBeneficioParaCondiciones", javaType=List.class, many = @Many(select = "obtenerCondicionesBeneficio"))  
            })
    List<Beneficio> obtenerBeneficiosDestacadosInteresUsuario (Integer idUsuario);
    
    /**
     * Obtiene lista de los últimos 9 visitados beneficios visitados por el usuario.
     * Si faltan items, la lista se completa con los beneficios más visitados de bennder.
     * @param idUsuario
     * @return 
     */
    @Select("(SELECT "
                + "B.ID_BENEFICIO AS idBeneficio, "
                + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
                + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
                + "B.TITULO, "
                + "B.CALIFICACION, "
                + "B.ID_TIPO_BENEFICIO, "
                + "B.ID_CATEGORIA AS idCategoria, "
                + "B.FECHA_EXPIRACION AS fechaExpiracion, "
                + "B.DESCRIPCION AS descripcion, "
                + "BP.PRECIO_NORMAL AS precioNormal, "
                + "BP.PRECIO_OFERTA AS precioOferta, "
                + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
                + "BG.GANCHO, "
                + "C.NOMBRE AS nombreCategoria, "
                + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN proveedor.CATEGORIA C ON C.ID_CATEGORIA = B.ID_CATEGORIA " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "INNER JOIN USUARIO_BENEFICIO UB ON B.ID_BENEFICIO = UB.ID_BENEFICIO AND UB.ID_USUARIO = #{idUsuario} AND UB.ID_ACCION_BENEFICIO = 0 " +
            "INNER JOIN FECHA_ACCION_BENEFICIO FAB ON UB.ID_BENEFICIO = FAB.ID_BENEFICIO AND UB.ID_USUARIO = FAB.ID_USUARIO " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION AND B.HABILITADO = TRUE AND B.STOCK > 0 " +
            "ORDER BY FAB.FECHA DESC)" +
            "UNION" +
            "(SELECT "
                + "B.ID_BENEFICIO AS idBeneficio, "
                + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
                + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
                + "B.TITULO, "
                + "B.CALIFICACION, "
                + "B.ID_TIPO_BENEFICIO, "
                + "B.ID_CATEGORIA AS idCategoria, "
                + "B.FECHA_EXPIRACION AS fechaExpiracion, "
                + "B.DESCRIPCION AS descripcion, "
                + "BP.PRECIO_NORMAL AS precioNormal, "
                + "BP.PRECIO_OFERTA AS precioOferta, "
                + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
                + "BG.GANCHO, "
                + "C.NOMBRE AS nombreCategoria, "
                + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN proveedor.CATEGORIA C ON C.ID_CATEGORIA = B.ID_CATEGORIA " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION AND B.HABILITADO = TRUE AND B.STOCK > 0 " +
            "ORDER BY VISITAS_GENERAL DESC)" +
            "LIMIT 9")
            @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficioParaImagenes", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficio")),
            @Result(property = "condiciones", column = "idBeneficioParaCondiciones", javaType=List.class, many = @Many(select = "obtenerCondicionesBeneficio"))  
            })
    List<Beneficio> obtenerUltimosBeneficiosVistosUsuario (Integer idUsuario);
    
    /**
     * Obtiene la lista de los nuevos beneficios agregados a bennder, con un máximo de 9 items.
     * Se toma como prioridad los beneficios con las categorìas elegidas por el usuario.
     * @param idUsuario
     * @return 
     */
    
    @Select("(SELECT "
                + "B.ID_BENEFICIO AS idBeneficio, "
                + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
                + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
                + "B.TITULO, "
                + "B.CALIFICACION, "
                + "B.ID_TIPO_BENEFICIO, "
                + "BP.PRECIO_NORMAL AS precioNormal, "
                + "BP.PRECIO_OFERTA AS precioOferta, "
                + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
                + "BG.GANCHO, "
                + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN INTERES_USUARIO IU ON B.ID_CATEGORIA = IU.ID_CATEGORIA AND IU.ID_USUARIO = #{idUsuario} " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION AND B.HABILITADO = TRUE AND B.STOCK > 0 " +
            "ORDER BY B.FECHA_CREACION DESC)" +
            "UNION" +
            "(SELECT "
                + "B.ID_BENEFICIO AS idBeneficio, "
                + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
                + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
                + "B.TITULO, "
                + "B.CALIFICACION, "
                + "B.ID_TIPO_BENEFICIO, "
                + "BP.PRECIO_NORMAL AS precioNormal, "
                + "BP.PRECIO_OFERTA AS precioOferta, "
                + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
                + "BG.GANCHO, "
                + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION AND B.HABILITADO = TRUE AND B.STOCK > 0 " +
            "ORDER BY B.FECHA_CREACION DESC)" +
            "LIMIT 9")
            @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficioParaImagenes", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficio")),
            @Result(property = "condiciones", column = "idBeneficioParaCondiciones", javaType=List.class, many = @Many(select = "obtenerCondicionesBeneficio"))  
            })
    List<Beneficio> obtenerNuevosBeneficiosInteresUsuario (Integer idUsuario);
    
    /***
     * Obtiene path location a nivel de servidor del logo del proveedor
     * @param idBeneficio identificador de beneficio
     * @return 
     */
    @Select("SELECT PATH_LOGO FROM proveedor.PROVEEDOR WHERE ID_PROVEEDOR =( " +
            "SELECT ID_PROVEEDOR FROM proveedor.BENEFICIO WHERE ID_BENEFICIO = #{idBeneficio} )")
    public String getPathLogoProveedorByBeneficio(Integer idBeneficio);
    
    
    
    @Update("UPDATE proveedor.BENEFICIO SET VISITAS_GENERAL = (SELECT COALESCE(VISITAS_GENERAL,0) + 1 FROM proveedor.BENEFICIO WHERE ID_BENEFICIO = #{idBeneficio}) " +
            "WHERE ID_BENEFICIO = #{idBeneficio} ")
    public void actualizarVisitasBeneficio(Integer idBeneficio);

    /**
     * Obtiene una lista de beneficios dado una busqueda de texto en el título y descripción del beneficio
     * @param busqueda string con texto a buscar (las palabras deben ir separadas por espacio
     * @return lista de beneficios List<Beneficio>
     */
    @Select("SELECT "
            + "B.ID_BENEFICIO AS idBeneficio, "
            + "B.ID_BENEFICIO AS idBeneficioParaImagenes, "
            + "B.ID_BENEFICIO AS idBeneficioParaCondiciones, "
            + "B.TITULO, B.CALIFICACION, "
            + "B.ID_TIPO_BENEFICIO, "
            + "BP.PRECIO_NORMAL AS precioNormal, "
            + "BP.PRECIO_OFERTA AS precioOferta, "
            + "BD.PORCENTAJE_DESCUENTO AS porcentajeDescuento, "
            + "BG.GANCHO, "
            + "P.NOMBRE AS nombreProveedor " +
            "FROM proveedor.BENEFICIO B " +
            "INNER JOIN proveedor.PROVEEDOR P ON B.ID_PROVEEDOR = P.ID_PROVEEDOR AND P.HABILITADO = TRUE " +
            "LEFT JOIN proveedor.BENEFICIO_PRODUCTO BP ON B.ID_BENEFICIO = BP.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_DESCUENTO BD ON B.ID_BENEFICIO = BD.ID_BENEFICIO " +
            "LEFT JOIN proveedor.BENEFICIO_GANCHO BG ON B.ID_BENEFICIO = BG.ID_BENEFICIO " +
            "WHERE "
            + "NOW() BETWEEN B.FECHA_INICIAL AND B.FECHA_EXPIRACION "
            + "AND B.HABILITADO = TRUE "
            + "AND B.STOCK > 0 "
            + "AND to_tsvector(B.titulo || B.descripcion) @@ plainto_tsquery(#{busqueda}) " +
            "ORDER BY B.FECHA_CREACION DESC")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficioParaImagenes", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficio")),
            @Result(property = "condiciones", column = "idBeneficioParaCondiciones", javaType=List.class, many = @Many(select = "obtenerCondicionesBeneficio"))
            })
    List<Beneficio> obtenerBeneficiosPorBusqueda(String busqueda);

    String QUERY_DETALLE_BENEFICIO = " SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " b.calificacion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM proveedor.beneficio b" +
            " INNER JOIN proveedor.tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor.proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN proveedor.beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN proveedor.beneficio_producto bp ON b.id_beneficio = bp.id_beneficio";

    @Select(QUERY_DETALLE_BENEFICIO +
            " WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre})" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND bd.porcentaje_descuento BETWEEN #{descuentoMin} AND #{descuentoMax}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatPadreFiltradosDescuento(@Param("idCategoriaPadre") Integer idCategoriaPadre, @Param("descuentoMin") Integer descuentoMin, @Param("descuentoMax") Integer descuentoMax);

    @Select(QUERY_DETALLE_BENEFICIO +
            " WHERE b.id_categoria = #{idCategoria}" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND bd.porcentaje_descuento BETWEEN #{descuentoMin} AND #{descuentoMax}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatFiltradosPorDescuento(@Param("idCategoria") Integer idCategoria,  @Param("descuentoMin") Integer descuentoMin, @Param("descuentoMax") Integer descuentoMax);

    @Select(QUERY_DETALLE_BENEFICIO +
            " WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre})" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND p.nombre = #{proveedor}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatPadreFiltradosProveedor(@Param("idCategoriaPadre")Integer idCategoriaPadre, @Param("proveedor") String proveedor);

    @Select(QUERY_DETALLE_BENEFICIO +
            " WHERE b.id_categoria = #{idCategoria}" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND p.nombre = #{proveedor}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatFiltradosPorProveedor(@Param("idCategoria") Integer idCategoria, @Param("proveedor") String proveedor);

    @Select(QUERY_DETALLE_BENEFICIO +
            " WHERE b.id_categoria in (SELECT id_categoria FROM proveedor.categoria WHERE id_categoria_padre = #{idCategoriaPadre})" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND b.calificacion = #{calificacion}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatPadreFiltradosCalificacion(@Param("idCategoriaPadre") Integer idCategoriaPadre, @Param("calificacion") Integer calificacion);

    @Select(QUERY_DETALLE_BENEFICIO +
            " WHERE b.id_categoria = #{idCategoria}" +
            " AND NOW() BETWEEN b.fecha_inicial AND b.fecha_expiracion " +
            " AND b.habilitado = TRUE " +
            " AND b.stock > 0" +
            " AND b.calificacion = #{calificacion}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "id_beneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosCatFiltradosPorCalificacion(@Param("idCategoria") Integer idCategoria, @Param("calificacion") Integer calificacion);
}
