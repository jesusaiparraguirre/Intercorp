package com.intercorp.myapplication.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonObject;
import com.intercorp.myapplication.data.network.Rest;
import com.intercorp.myapplication.databinding.FragmentRegisterBinding;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private Rest rest;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(verify()){
                    JsonObject body = setBodySearch(binding.name.getText().toString(),
                            binding.lastname.getText().toString(),
                            binding.age.getText().toString(),
                            binding.date.getText().toString());
                    rest= new Rest();
                    rest.getWebservices().generalMethod( body, new Callback<JsonObject>() {
                        @Override
                        public void success(JsonObject jsonObject, Response response) {
                            Snackbar.make(getView(), "Registro exitoso", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Snackbar.make(getView(), "Error al registrarse", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public JsonObject setBodySearch(String nombre, String apellido, String edad, String nacimiento){
        JsonObject body = new JsonObject();
        body.addProperty("nombre",nombre);
        body.addProperty("apellido", apellido);
        body.addProperty("edad",edad);
        body.addProperty("nacimiento",nacimiento);

        Log.e("body",body.toString());
        return body;
    }

    public boolean verify() {
        if(binding.name.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Ingrese su nombre", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(binding.lastname.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Ingrese su apellido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(binding.age.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Ingrese su edad", Toast.LENGTH_SHORT).show();
            return false;
        }if(binding.date.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "Ingrese su nacimiento", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}