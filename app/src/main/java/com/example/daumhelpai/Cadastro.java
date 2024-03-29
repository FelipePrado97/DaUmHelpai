package com.example.daumhelpai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;


public class Cadastro extends AppCompatActivity {

    DataBaseHelper helper = new DataBaseHelper(this);

    public EditText edtNome, edtEmail, edtSenha, edtConfSenha, edtCelular;
    public Button btnCadastrar, btnFoto;
    public ImageView imvFoto;
    public byte[] bArray;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private final int GALERIA_IMAGENS = 1;
    public AlertDialog dialog;
    public Bundle parametros;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        edtNome = findViewById(R.id.edtNome);
        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfSenha = findViewById(R.id.edtConfSenha);
        btnCadastrar = findViewById(R.id.btnAlterarCadastro);
        btnFoto = findViewById(R.id.btnFoto);
        imvFoto = findViewById(R.id.imvFoto);
        edtCelular = findViewById(R.id.edtCelular);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();
        if (parametros != null){
            String email = parametros.getString("email_do_usuario");
            String senha = parametros.getString("senha_do_usuario");

            edtEmail.setText(email);
            edtSenha.setText(senha);
        }

        //Criando a máscara para o celular
        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN)N NNNN - NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edtCelular, smf);
        edtCelular.addTextChangedListener(mtw);
        //FIM mascara Celular


    }



    public void cadastrar(View view) {
        String strNome = edtNome.getText().toString();
        String strEmail = edtEmail.getText().toString();
        String strSenha = edtSenha.getText().toString();
        String strConfSenha = edtConfSenha.getText().toString();
        String strCelular = edtCelular.getText().toString();



        int OP = helper.buscarUsuario(strEmail,strSenha);
        int OP2 = helper.buscaTelefone(strCelular);
        if (strSenha.equals(strConfSenha)){
            if ((strNome.isEmpty() || strEmail.isEmpty() || strSenha.isEmpty() ||strConfSenha.isEmpty() || strCelular.isEmpty()) == false){

                if (OP == 1){
                    Toast.makeText(Cadastro.this,"Usuário já Cadastrado", Toast.LENGTH_SHORT).show();
                }else {
                    if (OP2 == 0){
                        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
                        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
                            MyMessage();
                        }else{
                            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS},0);
                        }
                    }else{Toast.makeText(Cadastro.this,"Celular já Cadastrado", Toast.LENGTH_SHORT).show();
                    }

                }
            }else{
                Toast.makeText(Cadastro.this,"Todos os Campos São Obrigatórios", Toast.LENGTH_SHORT).show();

            }
        }else {
            Toast.makeText(Cadastro.this,"senha diferente de confirmação de senha", Toast.LENGTH_SHORT).show();
        }

    }

    private void MyMessage(){
        final String strCelular = edtCelular.getText().toString();

        SmsManager smsManager = SmsManager.getDefault();
        Random rdm = new Random();
        int number = rdm.nextInt(1000)+1;

        final String mensagem = Integer.toString(number);

        smsManager.sendTextMessage(strCelular, null, mensagem, null, null);

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Cadastro.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_confirma_telefone, null);

        final EditText mCode = mView.findViewById(R.id.edtCodeCell);
        Button mConfirmarCode = mView.findViewById(R.id.btnConfirmaCode);

        mConfirmarCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1515 e 1516 códigos para teste
                String CodeSMS = mCode.getText().toString();
                if (CodeSMS.equals(mensagem) || CodeSMS.equals("1515") || CodeSMS.equals("1516")) {
                    String strNome = edtNome.getText().toString();
                    String strEmail = edtEmail.getText().toString();
                    String strSenha = edtSenha.getText().toString();

                    helper.cadastrarbd(strNome, strEmail, strSenha, bArray, strCelular);

                    Toast toast = Toast.makeText(Cadastro.this, "Cadastrado Com Sucesso", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(Cadastro.this, Principal.class);
                    Bundle parametros = new Bundle();

                    parametros.putString("email_do_usuario", strEmail);

                    intent.putExtras(parametros);
                    startActivity(intent);
                }else{
                    Toast toast = Toast.makeText(Cadastro.this, "Código Incorreto", Toast.LENGTH_LONG);
                }

            }
        });
        mBuilder.setView(mView);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }
    public void imagem(View view){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Cadastro.this);
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
                    Toast.makeText(Cadastro.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
