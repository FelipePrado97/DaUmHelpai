package com.example.daumhelpai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditarPerfil extends AppCompatActivity {
    /*private static final String[] ESTADOS = new String[]{
            "AC|Acre", "AL|Alagoas", "AP|Amapá", "AM|Amazonas", "BA|Bahia", "CE|Ceará", "DF|Distrito Federal",
            "ES|Espírito Santo", "GO|Goiás", "MA|Maranhão", "MT|Mato Grosso", "MS|Mato Grosso do Sul", "MG|Minas Gerais",
            "PA|Pará", "PB|Paraíba", "PR|Paraná", "PE|Pernambuco", "PI|Piauí", "RJ|Rio de Janeiro", "RN|Rio Grande do Norte",
            "RS|Rio Grande do Sul", "RO|Rondônia", "RR|Roraima", "SC|Santa Catarina", "SP|São Paulo", "SE|Sergipe", "TO|Tocantins"
    };*/

    DataBaseHelper helper = new DataBaseHelper(this);
    public Bundle parametros;
    public EditText edtNome, edtSenha, edtConfSenha,
            edtNascimento,edtCEP, edtCidade, edtEstado,
            edtRua, edtBairro, edtNumero, edtComplmento;
    //public EditText edtEmail,edtCelular;
    public TextView txtEsqueciMeuCep;
    public Button btnFoto, btnAlterarCadastro;
    public RadioGroup rdgSexo;
    public RadioButton rdbMasc, rdbFem;
    public ImageView imvFoto;
    public byte[] bArray;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private final int GALERIA_IMAGENS = 1;
    public AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();

        edtNome = findViewById(R.id.edtNome);
        edtSenha = findViewById(R.id.edtSenha);
        edtConfSenha = findViewById(R.id.edtConfSenha);
        //edtEmail = findViewById(R.id.edtEmail);
        //edtCelular = findViewById(R.id.edtCelular);
        edtNascimento = findViewById(R.id.edtNascimento);
        edtCEP = findViewById(R.id.edtCEP);
        edtCidade = findViewById(R.id.edtCidade);
        edtEstado = findViewById(R.id.edtEstado);
        edtRua = findViewById(R.id.edtRua);
        edtBairro = findViewById(R.id.edtBairro);
        edtNumero = findViewById(R.id.edtNumero);
        edtComplmento = findViewById(R.id.edtComplemento);
        txtEsqueciMeuCep = findViewById(R.id.txtEsqueciMeuCep);
        btnFoto = findViewById(R.id.btnFoto);
        btnAlterarCadastro = findViewById(R.id.btnAlterarCadastro);
        rdgSexo = findViewById(R.id.rdgSexo);
        rdbMasc = findViewById(R.id.rdbMasc);
        rdbFem = findViewById(R.id.rdbFem);
        imvFoto = findViewById(R.id.imvFoto);

        //Criando a máscara para o celular
        SimpleMaskFormatter smf = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(edtNascimento, smf);
        edtNascimento.addTextChangedListener(mtw);
        //FIM mascara Celular

        String email = parametros.getString("email_do_usuario");
        Cursor cursor = helper.preencherPerfil(email);

        if (cursor != null){
            cursor.moveToFirst();
            edtNome.setText(cursor.getString(0));
            edtNascimento.setText(cursor.getString(1));
            //2 é o genero
            edtCidade.setText(cursor.getString(3));
            edtEstado.setText(cursor.getString(4));
            edtRua.setText(cursor.getString(5));
            edtBairro.setText(cursor.getString(6));
            edtNumero.setText(cursor.getString(7));
            edtCEP.setText(cursor.getString(8));
            edtComplmento.setText(cursor.getString(9));


            if (cursor.getString(2)==null){
                rdbMasc.setChecked(true);
            }else if (cursor.getString(2).equals("Masculino")){
                rdbMasc.setChecked(true);
            }else {
                rdbMasc.setChecked(false);
                rdbFem.setChecked(true);
            }

            Bitmap bm = helper.buscaFoto(email);
            if (bm != null) {
                imvFoto.setImageBitmap(bm);

            }

        }


    }

    public void tirarFoto(View view) {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }*/
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditarPerfil.this);
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
                    Toast.makeText(EditarPerfil.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void esquecimeucep(View view) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.buscacep.correios.com.br/sistemas/buscacep/buscaCepEndereco.cfm")));

    }

    public void alterarCadastro(View view) {
        String strNome =  edtNome.getText().toString();
        String strSenha = edtSenha.getText().toString();
        String strConfSenha = edtConfSenha.getText().toString();
        //String strEmail = edtEmail.getText().toString();
        String strNascimento = edtNascimento.getText().toString();
        String strCEP = edtCEP.getText().toString();
        String strBairro = edtBairro.getText().toString();
        String strEstado = edtBairro.getText().toString();
        String strCidade = edtCidade.getText().toString();
        String strRua = edtRua.getText().toString();
        String strNumero = edtNumero.getText().toString();
        String strComplemento = edtComplmento.getText().toString();
        String strSexo;

        if (rdbMasc.isChecked()){
            strSexo = "Masculino";
        }else{
            strSexo = "Feminino";
        }
        String email = parametros.getString("email_do_usuario");

        if ((strNome.isEmpty()||strSenha.isEmpty()||strConfSenha.isEmpty() || strNascimento.isEmpty()  ||
                strCEP.isEmpty() || strBairro.isEmpty() || strEstado.isEmpty() || strCidade.isEmpty() || strRua.isEmpty() ||
                strNumero.isEmpty() || strComplemento.isEmpty())==false){
            if (strSenha.equals(strConfSenha)){

                helper.alterarCadastro(strNome,strSenha,strNascimento,strCEP,strBairro,strEstado,strCidade,strRua,strNumero,strComplemento,bArray,email, strSexo);
                Toast toast = Toast.makeText(EditarPerfil.this,"Cadastro Alterado Com Sucesso", Toast.LENGTH_LONG);
                toast.show();

            }else{
                Toast toast = Toast.makeText(EditarPerfil.this,"Senha Diferente da Confirmação de Senha", Toast.LENGTH_SHORT);
                toast.show();
            }


        }else{
            Toast toast = Toast.makeText(EditarPerfil.this,"Todos os campos sao obrigatórios", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    public void abrirConfiguracoes(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(EditarPerfil.this,Configuracoes.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario",email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void abrirPrincipal(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(EditarPerfil.this, Principal.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }

    public void abrirMensagens(View view) {
        String email = parametros.getString("email_do_usuario");
        Intent intent = new Intent(EditarPerfil.this, Mensagens.class);
        Bundle parametros = new Bundle();

        parametros.putString("email_do_usuario", email);

        intent.putExtras(parametros);
        startActivity(intent);
    }
}
