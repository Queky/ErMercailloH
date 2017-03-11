package com.example.eneko.ermercailloh;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class articulosUsuario extends Fragment {

    ListaProductos lp = ListaProductos.getInstance();
    public articulosUsuario() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_articulos_usuario, container, false);
        return view;
    }

    public void onViewCreated(final View view, Bundle savedInstanceState) {

                final LinearLayout miVista = (LinearLayout) view.findViewById(R.id.LYN);
                final Usuario u1 = Usuario.getInstance();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8084/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Servicio service = retrofit.create(Servicio.class);

                //Call<Usuario> call = service.getUsuario(1);
                Call<List<Producto>> call = service.findAllByOwner(u1.getIdusuario());

                call.enqueue(new Callback<List<Producto>>() {
                    @Override
                    public void onResponse(Response<List<Producto>> response, Retrofit retrofit) {

                        try {

                            List<Producto> ListaProductos = response.body();

                            for (int i = 0; i < ListaProductos.size(); i++) {

                                if (i == 0) {
                                    Producto p1 = new Producto(ListaProductos.get(i).getIdproducto(),ListaProductos.get(i).getNombre(),
                                            ListaProductos.get(i).getDescripcion(),ListaProductos.get(i).getIdmaxpujador(),ListaProductos.get(i).getPujamaxima(),
                                            ListaProductos.get(i).getPropietario(),ListaProductos.get(i).getAbierto());
                                    lp.addProducto(p1);
                                    TextView tvID = new TextView(view.getContext());
                                    TextView tvDes= new TextView(view.getContext());
                                    TextView tvNom = new TextView(view.getContext());
                                    TextView tvPropietario= new TextView(view.getContext());
                                    TextView tvLine= new TextView(view.getContext());
                                    tvID.setText("Id del producto: "+p1.getIdproducto());
                                    miVista.addView(tvID);
                                    tvNom.setText("Nombre del producto: "+p1.getNombre());
                                    miVista.addView(tvNom);
                                    tvDes.setText("Descripcion:\n"+p1.getDescripcion());
                                    miVista.addView(tvDes);
                                    tvPropietario.setText("Propietario: "+u1.getNombre()+" "+u1.getApellido());
                                    miVista.addView(tvPropietario);
                                    tvLine.setText("-------------------------------------------------------");
                                    miVista.addView(tvLine);


                                } else  {
                                    Producto p1 = new Producto(ListaProductos.get(i).getIdproducto(),ListaProductos.get(i).getNombre(),
                                            ListaProductos.get(i).getDescripcion(),ListaProductos.get(i).getIdmaxpujador(),ListaProductos.get(i).getPujamaxima(),
                                            ListaProductos.get(i).getPropietario(),ListaProductos.get(i).getAbierto());
                                    lp.addProducto(p1);
                                    TextView tvID = new TextView(view.getContext());
                                    TextView tvDes= new TextView(view.getContext());
                                    TextView tvNom = new TextView(view.getContext());
                                    TextView tvPropietario= new TextView(view.getContext());
                                    TextView tvLine= new TextView(view.getContext());
                                    tvID.setText("Id del producto: "+p1.getIdproducto());
                                    miVista.addView(tvID);
                                    tvNom.setText("Nombre del producto: "+p1.getNombre());
                                    miVista.addView(tvNom);
                                    tvDes.setText("Descripcion:\n"+p1.getDescripcion());
                                    miVista.addView(tvDes);
                                    tvPropietario.setText("Propietario: "+u1.getNombre()+" "+u1.getApellido());
                                    miVista.addView(tvPropietario);
                                    tvLine.setText("-------------------------------------------------------");
                                    miVista.addView(tvLine);
                                }
                            }

                        } catch (Exception e) {
                            //.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                        // Log.d("onFailure", t.toString());
                    }
                });

            }



    }

