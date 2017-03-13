package com.example.eneko.ermercailloh;

import java.util.Date;

/**
 * Created by Eneko on 11/03/2017.
 */

public class Producto {
    private int idproducto;
    private String nombre;
    private String descripcion;
    private int propietario;
    private int idmaxpujador;
    private float pujamaxima;
    private int abierto;
    private long fechaapertura;
    private long fechacierre;

    public Producto(int idproducto, String nombre, String descripcion, int propietario, int idmaxpujador, float pujamaxima, int abierto, long fechaapertura, long fechacierre) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.propietario = propietario;
        this.idmaxpujador = idmaxpujador;
        this.pujamaxima = pujamaxima;
        this.abierto = abierto;
        this.fechaapertura = fechaapertura;
        this.fechacierre = fechacierre;
    }



    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getPropietario() {
        return propietario;
    }

    public void setPropietario(int propietario) {
        this.propietario = propietario;
    }

    public int getIdmaxpujador() {
        return idmaxpujador;
    }

    public void setIdmaxpujador(int idmaxpujador) {
        this.idmaxpujador = idmaxpujador;
    }

    public float getPujamaxima() {
        return pujamaxima;
    }

    public void setPujamaxima(float pujamaxima) {
        this.pujamaxima = pujamaxima;
    }

    public int getAbierto() {
        return abierto;
    }

    public void setAbierto(int abierto) {
        this.abierto = abierto;
    }

    public long getFechaapertura() {
        return fechaapertura;
    }

    public void setFechaapertura(long fechaapertura) {
        this.fechaapertura = fechaapertura;
    }

    public long getFechacierre() {
        return fechacierre;
    }

    public void setFechacierre(long fechacierre) {
        this.fechacierre = fechacierre;
    }




}
