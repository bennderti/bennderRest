package cl.bennder.bennderservices.model;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Diego on 03-03-2017.
 */
public class Beneficio implements Serializable{

    private Integer idBeneficio;
    private String titulo;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaExpiracion;
    private String condicion;
    private Boolean habilitado;
    private Integer calificacion;
    private Integer stock;
    private Integer idProveedor;
    private Integer idCategoria;
    private Integer idTipoBeneficio;
    private Integer limiteStock;
    private Integer visitasGeneral;

    public Beneficio() {
    }

    public Beneficio(Integer idBeneficio, String titulo, String descripcion, Date fechaCreacion, Date fechaExpiracion, String condicion, Boolean habilitado, Integer calificacion, Integer stock, Integer idProveedor, Integer idCategoria, Integer idTipoBeneficio, Integer limiteStock, Integer visitasGeneral) {
        this.idBeneficio = idBeneficio;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaExpiracion = fechaExpiracion;
        this.condicion = condicion;
        this.habilitado = habilitado;
        this.calificacion = calificacion;
        this.stock = stock;
        this.idProveedor = idProveedor;
        this.idCategoria = idCategoria;
        this.idTipoBeneficio = idTipoBeneficio;
        this.limiteStock = limiteStock;
        this.visitasGeneral = visitasGeneral;
    }

    public Integer getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(Integer idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Date getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(Date fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getCondicion() {
        return condicion;
    }

    public void setCondicion(String condicion) {
        this.condicion = condicion;
    }

    public Boolean getHabilitado() {
        return habilitado;
    }

    public void setHabilitado(Boolean habilitado) {
        this.habilitado = habilitado;
    }

    public Integer getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(Integer calificacion) {
        this.calificacion = calificacion;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Integer idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdTipoBeneficio() {
        return idTipoBeneficio;
    }

    public void setIdTipoBeneficio(Integer idTipoBeneficio) {
        this.idTipoBeneficio = idTipoBeneficio;
    }

    public Integer getLimiteStock() {
        return limiteStock;
    }

    public void setLimiteStock(Integer limiteStock) {
        this.limiteStock = limiteStock;
    }

    public Integer getVisitasGeneral() {
        return visitasGeneral;
    }

    public void setVisitasGeneral(Integer visitasGeneral) {
        this.visitasGeneral = visitasGeneral;
    }
}
