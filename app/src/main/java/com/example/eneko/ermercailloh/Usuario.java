package com.example.eneko.ermercailloh;


/**
 * Created by Eneko on 20/02/2017.
 */

public class Usuario {
    public int idusuario;
    public String apellido;
    public String nombre;
    public String email;
    public String password;
    public boolean estaLogueado;
    private static Usuario ourInstance = new Usuario();

    public static Usuario getInstance() {
        return ourInstance;
    }

    private Usuario() {
    }

    public int getIdusuario() {
        return idusuario;
    }

    public void setIdUsusario(int idUsusario) {
        this.idusuario = idUsusario;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public boolean isEstaLogueado() {
        return estaLogueado;
    }

    public void setEstaLogueado(boolean estaLogueado) {
        this.estaLogueado = estaLogueado;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void  setAtri(int idusuario, String nombre, String apellido, String email, String password) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void cerrarSesion(){
       estaLogueado = false;
    }
}