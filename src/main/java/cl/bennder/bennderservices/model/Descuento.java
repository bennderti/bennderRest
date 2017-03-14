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

    public Descuento(Integer idBeneficio, String titulo, String descripcion, Date fechaCreacion, Date fechaExperiacion, String condicion, Boolean habilitado, Integer calificacion, Integer stock, Integer idProveedor, Integer idCategoria, Integer idTipoBeneficio, Integer limiteStock, Integer visitasGeneral, Integer porcentajeDescuento) {
        super(idBeneficio, titulo, descripcion, fechaCreacion, fechaExperiacion, condicion, habilitado, calificacion, stock, idProveedor, idCategoria, idTipoBeneficio, limiteStock, visitasGeneral);
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Integer getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(Integer porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }
}
