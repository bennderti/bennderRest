package cl.bennder.bennderservices.model;

import java.util.Date;

/**
 * Created by Diego on 03-03-2017.
 */
public class Beneficio {

    private Integer idBeneficio;
    private String titulo;
    private String descripcion;
    private Date fechaCreacion;
    private Date fechaExperiacion;
    private String condicion;
    private Boolean habilitado;
    private Integer idCategoria;

    public Beneficio(Integer idBeneficio, String titulo, String descripcion, Date fechaCreacion, Date fechaExperiacion, String condicion, Boolean habilitado, Integer idCategoria) {
        this.idBeneficio = idBeneficio;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fechaCreacion = fechaCreacion;
        this.fechaExperiacion = fechaExperiacion;
        this.condicion = condicion;
        this.habilitado = habilitado;
        this.idCategoria = idCategoria;
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

    public Date getFechaExperiacion() {
        return fechaExperiacion;
    }

    public void setFechaExperiacion(Date fechaExperiacion) {
        this.fechaExperiacion = fechaExperiacion;
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

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }
}
