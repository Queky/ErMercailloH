package com.example.eneko.ermercailloh;

import java.util.Date;

/**
 * Created by Eneko on 11/03/2017.
 */

public class Producto {

    private int idProducto;
    private String estado;
    private int idPropietario;
    private String imgUri;
    private int precio;
    private String titulo;

    public Producto(int idProducto, String estado, int idPropietario, String imgUri, int precio, String titulo){
        idProducto = idProducto;
        estado = estado;
        idPropietario = idPropietario;
        imgUri = imgUri;
        precio = precio;
        titulo = titulo;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public String getImgUri() {
        return imgUri;
    }

    public int getPrecio() {
        return precio;
    }

    public String getTitulo() {
        return titulo;
    }
}