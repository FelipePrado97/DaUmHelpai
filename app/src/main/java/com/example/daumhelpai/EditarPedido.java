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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class EditarPedido extends AppCompatActivity {
    DataBaseHelper helper = new DataBaseHelper(this);
    public Bundle parametros;
    public String email;
    public ToggleButton tglTipo;
    public EditText edtTitulo, edtDesc, edtPagamento, edtPagOutros;
    public ImageView imvFoto, imvConfirmarHelp;
    public RadioButton rdbOutro, rdbDinheiro;
    public RadioGroup rdbGroup;
    public byte[] bArray;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private final int GALERIA_IMAGENS = 1;
    public AlertDialog dialog;
    public int idd;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);
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

        Bitmap bm;

        Intent intentrecebedora = getIntent();
        parametros = intentrecebedora.getExtras();

        int vetor_id[] = parametros.getIntArray("vetor_id");

        int id_pedido = parametros.getInt("pos");
        idd = vetor_id[id_pedido];


        Cursor cursor = helper.listarPedidoParaAlterar(idd);

        if (cursor!=null){
            //Toast.makeText(EditarPedido.this,"cursor tem dados",Toast.LENGTH_LONG).show();
            while (cursor.moveToNext()){
                edtTitulo.setText(cursor.getString(0));
                edtDesc.setText(cursor.getString(1));
                bArray = cursor.getBlob(2);
                bm = BitmapFactory.decodeByteArray(bArray,0,bArray.length);
                imvFoto.setImageBitmap(bm);
                if (cursor.getInt(4) == 1){
                    tglTipo.setChecked(true);
                }
                email = cursor.getString(5);
                String pagamento = cursor.getString(3);

                if (pagamento.contains("Pagamento: ")){
                    rdbOutro.setChecked(true);
                    edtPagOutros.setText(cursor.getString(3));
                }else{
                    rdbDinheiro.setChecked(true);
                    edtPagamento.setText(cursor.getString(3));
                }

            }
        }else {
            //Toast.makeText(EditarPedido.this,"cursor Não tem dados",Toast.LENGTH_LONG).show();
        }


    }

    public void tirarFoto(View view) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(EditarPedido.this);
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
                    Toast.makeText(EditarPedido.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void alterarHelp(View view) {

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

            helper.alterarHelp(strTitulo,strDesc,strPagamento,bArray,tipo,idd);


            Toast.makeText(EditarPedido.this,"Alteração Efetuada!",Toast.LENGTH_LONG).show();

            Intent intent = new Intent(EditarPedido.this, Configuracoes.class);
            Bundle parametros = new Bundle();

            parametros.putString("email_do_usuario", email);

            intent.putExtras(parametros);
            startActivity(intent);

        }else{
            Toast.makeText(EditarPedido.this,"Todos os Campos Devem ser Preenchidos!",Toast.LENGTH_LONG).show();
        }
    }
}
