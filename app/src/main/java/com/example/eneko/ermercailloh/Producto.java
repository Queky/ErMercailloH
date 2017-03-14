package com.example.eneko.ermercailloh;

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
    private String imagenuri;

    public Producto(int idproducto, String nombre, String descripcion, String imagenuri, int propietario, int idmaxpujador, float pujamaxima, int abierto, long fechaapertura, long fechacierre) {
        this.idproducto = idproducto;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.propietario = propietario;
        this.idmaxpujador = idmaxpujador;
        this.pujamaxima = pujamaxima;
        this.abierto = abierto;
        this.fechaapertura = fechaapertura;
        this.fechacierre = fechacierre;
        this.imagenuri = imagenuri;
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

    public String getImagenuri() {return imagenuri;}

    public void setImagenuri(String imagenuri) {this.imagenuri = imagenuri;}
}