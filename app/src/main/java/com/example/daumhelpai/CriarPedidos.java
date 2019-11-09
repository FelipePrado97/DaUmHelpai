package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;

public class CriarPedidos extends AppCompatActivity {


    DataBaseHelper helper = new DataBaseHelper(this);


    public ToggleButton tglTipo;
    public EditText edtTitulo, edtDesc, edtPagamento, edtPagOutros;
    public ImageView imvFoto, imvConfirmarHelp;
    public RadioButton rdbOutro, rdbDinheiro;
    public RadioGroup rdbGroup;
    public byte[] bArray;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    public Bundle parametros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_pedidos);
        tglTipo = findViewById(R.id.tglTipo);
        edtPagOutros = findViewById(R.id.edtPagOutros);
        edtTitulo = findViewById(R.id.edtTítulo);
        edtDesc = findViewById(R.id.edtDesc);
        edtPagamento = findViewById(R.id.edtPagamento);
        imvFoto = findViewById(R.id.imvAdicionarFoto);
        imvConfirmarHelp = findViewById(R.id.imvConfirmarHelp);
        rdbGroup = findViewById(R.id.rdbgroup);
        rdbOutro = findViewById(R.id.rdbOutro);
        rdbDinheiro = findViewById(R.id.rdbDinheiro);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();




        rdbOutro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPagamento.setEnabled(false);
                edtPagamento.setBackgroundColor(Color.GRAY);
                edtPagOutros.setEnabled(true);
                edtPagOutros.setVisibility(View.VISIBLE);
            }
        });
        rdbDinheiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtPagamento.setEnabled(true);
                edtPagamento.setBackgroundColor(Color.WHITE);
                edtPagOutros.setEnabled(false);
                edtPagOutros.setVisibility(View.INVISIBLE);
            }
        });


    }



    public void cadastrarHelp(View view) {
        String email = parametros.getString("email_do_usuario");

        String strTitulo = edtTitulo.getText().toString();
        String strDesc = edtDesc.getText().toString();
        String strPagamento;
        Boolean  tipo = tglTipo.isChecked();

        if (rdbDinheiro.isChecked()){
            strPagamento = edtPagamento.getText().toString();
        }else{
            strPagamento = edtPagOutros.getText().toString();
        }

        if ((strTitulo.isEmpty() || strDesc.isEmpty() || strPagamento.isEmpty() || bArray == null)== false){

            helper.cadastrarHelp(strTitulo,strDesc,strPagamento,bArray,tipo,email);


            Toast.makeText(CriarPedidos.this,"Cadastro Efetuado!",Toast.LENGTH_LONG).show();
            edtTitulo.setText("");
            edtDesc.setText("");
            edtPagamento.setText("");
            edtPagOutros.setText("");

        }else{
            Toast.makeText(CriarPedidos.this,"Todos os Campos Devem ser Preenchidos!",Toast.LENGTH_LONG).show();
        }

    }

    public void tirarFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imvFoto.setImageBitmap(imageBitmap);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
            bArray = bos.toByteArray();
        }
    }
}
