package cl.bennder.bennderservices.model;

import java.io.Serializable;

/**
 * Created by Diego on 15-03-2017.
 */
public class Producto extends Beneficio implements Serializable{

    private Integer precioNormal;
    private Integer precioOferta;

    public Producto() {
    }

    public Integer getPrecioNormal() {
        return precioNormal;
    }

    public void setPrecioNormal(Integer precioNormal) {
        this.precioNormal = precioNormal;
    }

    public Integer getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(Integer precioOferta) {
        this.precioOferta = precioOferta;
    }
}
