package com.example.daumhelpai;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DataBaseHelper helper = new  DataBaseHelper(this);

    public EditText edtEmail, edtSenha;
    public Button btnLogar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtEmail = findViewById(R.id.edtEmail);
        edtSenha = findViewById(R.id.edtSenha);
        btnLogar = findViewById(R.id.btnLogin);


    }

    public void logar(View view) {
        Toast mensagem;
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();

        int result;
        result = helper.buscarUsuario(email,senha);
        if (result == 0){
            //usuario ou senha não encontrado
            mensagem = Toast.makeText(MainActivity.this, "Usuário ou senha Não encontrado", Toast.LENGTH_SHORT);
            mensagem.show();
        }else{
            Intent intent = new Intent(MainActivity.this,Principal.class);
            Bundle parametros = new Bundle();

            parametros.putString("email_do_usuario",email);

            intent.putExtras(parametros);
            startActivity(intent);


            mensagem = Toast.makeText(MainActivity.this, "Login Bem Sucedido!", Toast.LENGTH_SHORT);
            mensagem.show();
        }

    }

    public void cadastrar(View view) {
        String email = edtEmail.getText().toString();
        String senha = edtSenha.getText().toString();
        Intent intent = new Intent(MainActivity.this,Cadastro.class);
        if ((email.isEmpty() || senha.isEmpty())== false){

            Bundle parametros = new Bundle();

            parametros.putString("email_do_usuario",email);
            parametros.putString("senha_do_usuario",senha);

            intent.putExtras(parametros);
            startActivity(intent);
        }else{
            startActivity(intent);
        }



    }
}
