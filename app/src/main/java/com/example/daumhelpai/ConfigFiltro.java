package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class ConfigFiltro extends AppCompatActivity {


    public Bundle parametros;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config_filtro);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();
    }
}
