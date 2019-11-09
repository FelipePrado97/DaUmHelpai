package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EditarPerfil extends AppCompatActivity {

    public Bundle parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();
    }

    public void tirarFoto(View view) {
    }
}
