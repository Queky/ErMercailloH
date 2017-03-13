package com.example.eneko.ermercailloh;

/**
 * Created by Eneko on 20/02/2017.
 */
public class Usuario {
    public String email;
    public String contraseña;
    public boolean estaLogueado;
    private static Usuario ourInstance = new Usuario();

    public static Usuario getInstance() {
        return ourInstance;
    }

    private Usuario() {
        email="eko@gmail.com";
        contraseña= "1234";
        estaLogueado = false;
    }
}
