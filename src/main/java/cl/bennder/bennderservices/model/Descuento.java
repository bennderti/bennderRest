package cl.bennder.bennderservices.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Diego on 10-03-2017.
 */
public class Descuento extends Beneficio implements Serializable{

    private Integer porcentajeDescuento;

    public Descuento() {
    }

    public Descuento(Integer idBeneficio, String titulo, String descripcion, Date fechaCreacion, Date fechaExpiracion, String condicion, Boolean habilitado, Integer calificacion, Integer stock, Integer idProveedor, Integer idCategoria, TipoBeneficio tipoBeneficio, Integer limiteStock, Integer visitasGeneral, Integer porcentajeDescuento) {
        super(idBeneficio, titulo, descripcion, fechaCreacion, fechaExpiracion, condicion, habilitado, calificacion, stock, idProveedor, idCategoria, tipoBeneficio, limiteStock, visitasGeneral);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Integer getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Integer porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
