package com.example.daumhelpai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class CriarPedidos extends AppCompatActivity {


    DataBaseHelper helper = new DataBaseHelper(this);


    public ToggleButton tglTipo;
    public EditText edtTitulo, edtDesc, edtPagamento, edtPagOutros;
    public ImageView imvFoto, imvConfirmarHelp;
    public RadioButton rdbOutro, rdbDinheiro;
    public RadioGroup rdbGroup;
    public byte[] bArray;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private final int GALERIA_IMAGENS = 1;
    public AlertDialog dialog;
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


            strPagamento = "R$: "+edtPagamento.getText().toString();

        }else{
            strPagamento = "Pagamento: "+edtPagOutros.getText().toString();
        }

        if ((strTitulo.isEmpty() || strDesc.isEmpty() || strPagamento.isEmpty() || bArray == null)== false){

            helper.cadastrarHelp(strTitulo,strDesc,strPagamento,bArray,tipo,email);


            Toast.makeText(CriarPedidos.this,"Cadastro Efetuado!",Toast.LENGTH_LONG).show();
            edtTitulo.setText("");
            edtDesc.setText("");
            edtPagamento.setText("");
            edtPagOutros.setText("");

            Intent intent = new Intent(CriarPedidos.this, Principal.class);
            Bundle parametros = new Bundle();

            parametros.putString("email_do_usuario", email);

            intent.putExtras(parametros);
            startActivity(intent);

        }else{
            Toast.makeText(CriarPedidos.this,"Todos os Campos Devem ser Preenchidos!",Toast.LENGTH_LONG).show();
        }

    }

    public void tirarFoto(View view) {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(CriarPedidos.this);
        View mView = getLayoutInflater().inflate(R.layout.galeria_foto, null);

        LinearLayout lFoto = mView.findViewById(R.id.linearFoto);
        LinearLayout lGaleria = mView.findViewById(R.id.linearGaleria);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
        dialog.show();

        lFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                dialog.dismiss();

            }
        });

        lGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, GALERIA_IMAGENS);
                dialog.dismiss();
            }
        });


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
        if (requestCode == GALERIA_IMAGENS) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    // Salvando a imagem
                    imvFoto.setImageBitmap(bitmap);
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,bos);
                    bArray = bos.toByteArray();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CriarPedidos.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void abrirConfiguracoes(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(CriarPedidos.this,Configuracoes.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario",email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void abrirPrincipal(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(CriarPedidos.this, Principal.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void abrirMensagens(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(CriarPedidos.this, Mensagens.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }
}
