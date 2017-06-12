package com.example.eneko.ermercailloh;

import java.math.BigInteger;

/**
 * Created by Eneko on 13/03/2017.
 */

public class Puja {

    private  String tituloProducto;
    private float precioInicial;
    private float miPrecioPujado;
    private float pujaMaximaArticulo;
    private int idProducto;
    private String imgUri;

    public String getImgUri() {
        return imgUri;
    }

    public void setImgUri(String imgUri) {
        this.imgUri = imgUri;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public Puja() {
    }



    public String getTituloProducto() {
        return tituloProducto;
    }

    public void setTituloProducto(String tituloProducto) {
        this.tituloProducto = tituloProducto;
    }

    public float getPrecioInicial() {
        return precioInicial;
    }

    public void setPrecioInicial(float precioInicial) {
        this.precioInicial = precioInicial;
    }

    public float getMiPrecioPujado() {
        return miPrecioPujado;
    }

    public void setMiPrecioPujado(float miPrecioPujado) {
        this.miPrecioPujado = miPrecioPujado;
    }

    public float getPujaMaximaArticulo() {
        return pujaMaximaArticulo;
    }

    public void setPujaMaximaArticulo(float pujaMaximaArticulo) {
        this.pujaMaximaArticulo = pujaMaximaArticulo;
    }

    public Puja( String tituloProducto, float precioInicial, float miPrecioPujado, float pujaMaximaArticulo) {


        this.tituloProducto = tituloProducto;
        this.precioInicial = precioInicial;
        this.miPrecioPujado = miPrecioPujado;
        this.pujaMaximaArticulo = pujaMaximaArticulo;
    }
}
