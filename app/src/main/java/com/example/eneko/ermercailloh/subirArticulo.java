package com.example.eneko.ermercailloh;


import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class subirArticulo extends Fragment {
    Button b1;
    Button btnImgUpload;
    Usuario u1;
    EditText txtNombre;
    EditText txtDescripcion;
    EditText txtFechaApertura;
    EditText txtFechaCierre;
    String encoded = "base64,";

    // He tenido crear esto para que se pueda mostrar la imagen en la app al elegirla
    View view;

    private int PICK_IMAGE_REQUEST = 1;

    public subirArticulo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_subir_articulo, container, false);
        this.view = view;
        return view;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        txtNombre = (EditText) view.findViewById(R.id.editNombreSA);

        txtDescripcion= (EditText) view.findViewById(R.id.editDesSA);
        txtFechaApertura = (EditText) view.findViewById(R.id.editFaperSA);
        txtFechaCierre= (EditText) view.findViewById(R.id.editFcierreSA);



        b1 =(Button)view.findViewById(R.id.buttonSubirArt);

        u1 = Usuario.getInstance();
        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8084/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final Servicio service = retrofit.create(Servicio.class);

                Producto p1 = new Producto(1,"eeee","eeee",encoded,1,0,0,1,1489256985955l,1489256985955l);
                //Log.d("subida imagen producto", encoded);
                Call<Producto> call = service.create(p1);

                call.enqueue(new Callback<Producto>() {
                    @Override
                    public void onResponse(Response<Producto> response, Retrofit retrofit) {

                        try {

                        } catch (Exception e) {
                            //Log.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Throwable t) {

                        // Log.d("onFailure", t.toString());
                    }
                });

            }
        });

        btnImgUpload = (Button)view.findViewById(R.id.buttonSubirImg);
        btnImgUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://10.0.2.2:8084/erMercailloHSW/rest/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Servicio service = retrofit.create(Servicio.class);

                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Elige una imagen"), PICK_IMAGE_REQUEST);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                // Mostrar imagen
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                ImageView imageView = (ImageView)view.findViewById(R.id.imagenArticulo);
                imageView.setImageBitmap(bitmap);
                // Encode to base64
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                encoded += Base64.encodeToString(byteArray, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

