/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.model;

import java.util.Date;

/**
 *
 * @author dyanez
 */
public class UsuarioBeneficio {
    private Integer idBeneficio;
    private Integer idUsuario;
    private Integer idAccionBeneficio;
    private Integer cantidad;
    private String codigoBeneficio;
    private String codigoBeneficioEncriptado;
    private Date fecha;
    private Integer idVendedorPOS;
    private Integer idSucursalcanje;

    public UsuarioBeneficio() {
    }

    public UsuarioBeneficio(Integer idBeneficio, Integer idUsuario, Integer idAccionBeneficio, Integer cantidad, String codigoBeneficio, String codigoBeneficioEncriptado, Date fecha, Integer idVendedorPOS) {
        this.idBeneficio = idBeneficio;
        this.idUsuario = idUsuario;
        this.idAccionBeneficio = idAccionBeneficio;
        this.cantidad = cantidad;
        this.codigoBeneficio = codigoBeneficio;
        this.codigoBeneficioEncriptado = codigoBeneficioEncriptado;
        this.fecha = fecha;
        this.idVendedorPOS = idVendedorPOS;
    }

    

   

    public Integer getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(Integer idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Integer getIdAccionBeneficio() {
        return idAccionBeneficio;
    }

    public void setIdAccionBeneficio(Integer idAccionBeneficio) {
        this.idAccionBeneficio = idAccionBeneficio;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getCodigoBeneficio() {
        return codigoBeneficio;
    }

    public void setCodigoBeneficio(String codigoBeneficio) {
        this.codigoBeneficio = codigoBeneficio;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCodigoBeneficioEncriptado() {
        return codigoBeneficioEncriptado;
    }

    public void setCodigoBeneficioEncriptado(String codigoBeneficioEncriptado) {
        this.codigoBeneficioEncriptado = codigoBeneficioEncriptado;
    }

    public Integer getIdVendedorPOS() {
        return idVendedorPOS;
    }

    public void setIdVendedorPOS(Integer idVendedorPOS) {
        this.idVendedorPOS = idVendedorPOS;
    }

    public Integer getIdSucursalcanje() {
        return idSucursalcanje;
    }

    public void setIdSucursalcanje(Integer idSucursalcanje) {
        this.idSucursalcanje = idSucursalcanje;
    }

    @Override
    public String toString() {
        return "UsuarioBeneficio{" + "idBeneficio=" + idBeneficio + ", idUsuario=" + idUsuario + ", idAccionBeneficio=" + idAccionBeneficio + ", cantidad=" + cantidad + ", codigoBeneficio=" + codigoBeneficio + ", codigoBeneficioEncriptado=" + codigoBeneficioEncriptado + ", fecha=" + fecha + ", idVendedorPOS=" + idVendedorPOS + ", idSucursalcanje=" + idSucursalcanje + '}';
    }
    
    
    
    
}
