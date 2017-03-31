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
     * Obtiene el título del beneficio para completar asunto cuando se envia correo de beneficio seleccionado
     * @param idBeneficio iddentificador de beneficio
     * @author dyanez
     * @return 
     */
    @Select("select titulo from beneficio where id_beneficio=#{idBeneficio}")
    public String getTituloBeneficioAsuntoEnvioCorreo(Integer idBeneficio);
    
    /***
     * Obtiene el stock actual del beneficio seleccionado
     * @author dyanez
     * @param idBeneficio identificador de beneficio
     * @return Stock beneficio
     */
    @Select("select stock from beneficio where id_beneficio= #{idBeneficio}")
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
    @Update("UPDATE BENEFICIO SET STOCK = (SELECT STOCK FROM BENEFICIO WHERE ID_BENEFICIO = #{idBeneficio}) - #{cantidad} WHERE ID_BENEFICIO = #{idBeneficio};")
    public void descuentaStockBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Inserta accion de usuario beneficio
     * @param uBeneficio 
     */
    @Insert("INSERT INTO usuario_beneficio( " +
"            id_usuario, id_beneficio, id_accion_beneficio, cantidad, codigo_beneficio,codigo_beneficio_encriptado) " +
"    VALUES (#{idUsuario}, #{idBeneficio}, #{idAccionBeneficio}, #{cantidad}, #{codigoBeneficio},#{codigoBeneficioEncriptado})")
    public void guardarUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Actualiza la ultima accion del usuario sobre beneficio en la tabla de negocio
     * @param uBeneficio 
     */
    @Update("UPDATE usuario_beneficio " +
    "   SET  id_accion_beneficio= #{idAccionBeneficio} " +
    " WHERE id_usuario = #{idUsuario} AND id_beneficio = #{idBeneficio}")
    public void actualizaAccionUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    @Update("UPDATE fecha_accion_beneficio " +
    "   SET  FECHA = now() " +
    " WHERE id_usuario = #{idUsuario} AND id_beneficio = #{idBeneficio} AND id_accion_beneficio = #{idAccionBeneficio}")
    public void actualizaFechaAccionUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Inseerta fecha asociada a la acccion del usuario sobre beneficio/auditoria
     * @param uBeneficio 
     */
    @Insert("INSERT INTO fecha_accion_beneficio( " +
"            id_usuario, id_beneficio, id_accion_beneficio, fecha) " +
"    VALUES (#{idUsuario}, #{idBeneficio}, #{idAccionBeneficio}, now())")
    public void insertaFechaAccionUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    /***
     * Registra fecha accion de beneficio/auditoria sobre estado de beneficio
     * @param uBeneficio
     * @return 
     */
    @Select("SELECT count(1) FROM fecha_accion_beneficio "
            + " WHERE id_usuario = #{idUsuario} "
            + " AND id_beneficio = #{idBeneficio} "
            + " id_accion_beneficio = #{idAccionBeneficio}")
    public Integer getFechaUsuarioBeneficio(UsuarioBeneficio uBeneficio);
    
    
    
    //https://www.postgresql.org/docs/8.1/static/functions-datetime.html 
    /***
     * Valida si cliente ya habia obtenido determinado beneficio dentro de fecha de vigencia
     * @param idUsuario Identificador de usuario
     * @param idBeneficio Identificador de beneficio
     * @return 
     */
    @Select("SELECT COUNT(1) FROM BENEFICIO B INNER JOIN USUARIO_BENEFICIO UB ON UB.ID_BENEFICIO = B.ID_BENEFICIO " +
            "WHERE current_date <=  B.fecha_expiracion AND UB.ID_BENEFICIO = #{b} AND UB.ID_USUARIO = #{u}")
    public Integer usuarioHaObtenidoCuponbeneficio(@Param("u") Integer idUsuario,@Param("b") Integer idBeneficio);
    
    /***
     * Obtiene los beneficios de una categoria seleccionda para el cargador (datos simples)
     * @param idCategoria Identificador de la categoria
     * @return 
     */
    @Select("SELECT ID_BENEFICIO AS idBeneficio,TITULO as titulo FROM BENEFICIO WHERE ID_CATEGORIA = #{idCategoria}")
    List<BeneficioCargador> getBeneficiosCargadorByIdCat(Integer idCategoria);

    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM beneficio b" +
            " INNER JOIN tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria = #{idCategoria}")
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
    List<Beneficio> obtenerBeneficiosPorCategoria(Integer idCategoria);


    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.id_beneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM beneficio b" +
            " INNER JOIN tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_categoria in (SELECT id_categoria FROM categoria WHERE id_categoria_padre = #{idCategoriaPadre})")
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
    List<Beneficio> obtenerBeneficiosPorCategoriaPadre(Integer idCategoriaPadre);

    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta," +
            " p.nombre as nombreProveedor" +
            " FROM beneficio b" +
            " INNER JOIN tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
            " INNER JOIN proveedor p ON p.id_proveedor = b.id_proveedor" +
            " LEFT JOIN beneficio_descuento bd ON b.id_beneficio = bd.id_beneficio" +
            " LEFT JOIN beneficio_producto bp ON b.id_beneficio = bp.id_beneficio" +
            " WHERE b.id_beneficio = #{idBeneficio}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficio")),

    })
    Beneficio obtenerDetalleBeneficio(Integer idBeneficio);

    @Select(" SELECT imagen " +
            " FROM beneficio_imagen" +
            " WHERE id_beneficio = #{idBeneficio}")
    List<BeneficioImagen> obtenerImagenesBeneficio(Integer idBeneficio);

    @Select(" SELECT imagen " +
            " FROM beneficio_imagen" +
            " WHERE id_beneficio = #{idBeneficio}" +
            " AND orden in (1,2)")
    List<BeneficioImagen> obtenerImagenesBeneficioPreview(Integer idBeneficio);
}
