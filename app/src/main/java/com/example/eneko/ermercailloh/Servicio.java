package com.example.eneko.ermercailloh;

/**
 * Created by Eneko on 10/03/2017.
 */

import java.util.List;
import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


public interface Servicio {
    @GET("usuario/{id}")
    Call<Usuario> getUsuario(@Path("id") int usuario);

    @GET("usuario/buscar/{email}/{pass}")
    Call<List<Usuario>> findByEmailAndPass(@Path("email") String email, @Path("pass") String pass);

    @GET("producto/propietario/{id}")
    Call<List<Producto>> findAllByOwner(@Path("id") int id);

    @POST("producto")
    Call<Producto> create(@Body Producto producto);
}
