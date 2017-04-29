/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.bennder.bennderservices.constantes;

/**
 * Accion del usuario sobre beneficio seleccionado
 * @author dyanez
 */
public abstract class AccionBeneficioUsuario {
    public static Integer VISITADO = 0;//Cuando usuario pincha beneficio para visualizar
    public static Integer OBTENIDO = 1;//cuando usuario selecciona beneficio
    public static Integer DESCARGADO = 2;//cuando usuario ha descargado y generado cupon beneficio
    public static Integer CANJEADO = 3;//cuando usuario ha canjeado o ha hecho efectivo beneficio en POS de sucursal
}
