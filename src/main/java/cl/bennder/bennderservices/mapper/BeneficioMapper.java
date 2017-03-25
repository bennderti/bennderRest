package cl.bennder.bennderservices.mapper;

import cl.bennder.entitybennderwebrest.model.Beneficio;
import cl.bennder.entitybennderwebrest.model.Descuento;
import cl.bennder.entitybennderwebrest.model.Producto;
import cl.bennder.entitybennderwebrest.model.TipoBeneficio;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.IntegerTypeHandler;
import org.apache.ibatis.type.StringTypeHandler;

import java.util.List;

/**
 * Created by Diego on 10-03-2017.
 */
public interface BeneficioMapper {

    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta" +
            " FROM beneficio b" +
            " INNER JOIN tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
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

    })
    List<Beneficio> obtenerBeneficiosPorCategoria(Integer idCategoria);


    @Select(" SELECT b.id_beneficio AS idBeneficio," +
            " b.titulo, " +
            " b.descripcion," +
            " tb.id_tipo_beneficio," +
            " tb.nombre," +
            " bd.porcentaje_descuento as porcentajeDescuento," +
            " bp.precio_normal as precioNormal," +
            " bp.precio_oferta as precioOferta" +
            " FROM beneficio b" +
            " INNER JOIN tipo_beneficio tb ON tb.id_tipo_beneficio = b.id_tipo_beneficio " +
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

    })
    List<Beneficio> obtenerBeneficiosPorCategoriaPadre(Integer idCategoriaPadre);
}
