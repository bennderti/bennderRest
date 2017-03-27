package cl.bennder.bennderservices.mapper;

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
     * Obtiene los beneficios de una categoria seleccionda para el cargador (datos simples)
     * @param idCategoria Identificador de la categoria
     * @return 
     */
    @Select("SELECT ID_BENEFICIO AS idBeneficio,TITULO as titulo FROM BENEFICIO WHERE ID_CATEGORIA = #{idCategoria}")
    List<BeneficioCargador> getBeneficiosCargadorByIdCat(Integer idCategoria);

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
            " WHERE b.id_categoria = #{idCategoria}")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
            @Case(value = "1", type = Descuento.class),
            @Case(value = "2", type = Producto.class)

    })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
    })
    List<Beneficio> obtenerBeneficiosPorCategoria(Integer idCategoria);


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
            " WHERE b.id_categoria in (SELECT id_categoria FROM categoria WHERE id_categoria_padre = #{idCategoriaPadre})")
    @TypeDiscriminator(column = "id_tipo_beneficio",
            cases = {
                    @Case(value = "1", type = Descuento.class),
                    @Case(value = "2", type = Producto.class)

            })
    @Results({
            @Result(property = "tipoBeneficio.idTipoBeneficio", column = "id_tipo_beneficio", javaType = TipoBeneficio.class, typeHandler = IntegerTypeHandler.class),
            @Result(property = "tipoBeneficio.nombre", column = "nombre", javaType = TipoBeneficio.class, typeHandler = StringTypeHandler.class),
            @Result(property = "imagenesBeneficio", column = "idBeneficio", javaType=List.class, many = @Many(select = "obtenerImagenesBeneficioPreview")),
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
