package com.example.eneko.ermercailloh;

import java.math.BigInteger;

/**
 * Created by Eneko on 13/03/2017.
 */

public class Puja {
    private Integer idpuja;
    private Integer idproducto;
    private Integer idususario;
    private float preciopuja;
    private long fechapuja;


    public Puja(Integer idpuja, Integer idproducto, Integer idususario, float preciopuja, long fechapuja) {
        this.idpuja = idpuja;
        this.idproducto = idproducto;
        this.idususario = idususario;
        this.preciopuja = preciopuja;
        this.fechapuja = fechapuja;
    }

    public Integer getIdpuja() {
        return idpuja;
    }

    public void setIdpuja(Integer idpuja) {
        this.idpuja = idpuja;
    }

    public Integer getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Integer idproducto) {
        this.idproducto = idproducto;
    }

    public Integer getIdususario() {
        return idususario;
    }

    public void setIdususario(Integer idususario) {
        this.idususario = idususario;
    }

    public float getPreciopuja() {
        return preciopuja;
    }

    public void setPreciopuja(float preciopuja) {
        this.preciopuja = preciopuja;
    }

    public long getFechapuja() {
        return fechapuja;
    }

    public void setFechapuja(long fechapuja) {
        this.fechapuja = fechapuja;
    }
}
