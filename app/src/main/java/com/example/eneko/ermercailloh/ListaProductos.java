package com.example.eneko.ermercailloh;

import java.util.ArrayList;

/**
 * Created by Eneko on 11/03/2017.
 */
public class ListaProductos {

    private ArrayList<Producto> listaProductos;
    private static ListaProductos instance = new ListaProductos();

    public static ListaProductos getInstance(){
        return instance;
    }

    public ArrayList<Producto> getListaProductos(){ return listaProductos;}

    private ListaProductos(){
        listaProductos = new ArrayList<>();
    }

    public void addProducto(Producto p1){
        listaProductos.add(p1);
    }

    public boolean listaVacia(){
        if (listaProductos.isEmpty())
            return true;
        else
            return false;
    }
}
