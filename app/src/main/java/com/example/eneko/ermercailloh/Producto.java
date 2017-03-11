package com.example.eneko.ermercailloh;

import android.text.method.DateTimeKeyListener;

import java.util.Date;

/**
 * Created by Eneko on 11/03/2017.
 */

public class Producto {
    private int idproducto;
    private String nombre;
    private String descripcion;
    private int idmaxpujador;
    private float pujamaxima;
    private int propietario;
    private int abierto;
    private Date dInicio;
    private Date dFin;

    public Producto(int idproducto, String nombre, String descripcion, int idmaxpujador, float pujamaxima, int propietario, int abierto, Date dInicio, Date dFin) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idmaxpujador = idmaxpujador;
        this.pujamaxima = pujamaxima;
        this.propietario = propietario;
        this.abierto = abierto;
        this.dInicio = dInicio;
        this.dFin = dFin;
    }

    public Date getdInicio() {
        return dInicio;
    }

    public void setdInicio(Date dInicio) {
        this.dInicio = dInicio;
    }

    public Date getdFin() {
        return dFin;
    }

    public void setdFin(Date dFin) {
        this.dFin = dFin;
    }

    public int getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(int idproducto) {
        this.idproducto = idproducto;
    }

    public int getAbierto() {
        return abierto;
    }

    public void setAbierto(int abierto) {
        this.abierto = abierto;
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

    public int getPropietario() {
        return propietario;
    }

    public void setPropietario(int propietario) {
        this.propietario = propietario;
    }

    public Producto(int idproducto, String nombre, String descripcion, int idmaxpujador, float pujamaxima, int propietario, int abierto) {

        this.idproducto = idproducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.idmaxpujador = idmaxpujador;
        this.pujamaxima = pujamaxima;
        this.propietario = propietario;
        this.abierto = abierto;
    }
}
