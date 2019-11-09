package com.example.daumhelpai;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String NOME_BANCO = "banco2.db";
    private static final String TABELA = "usuarios";
    private static final String ID = "id";
    private static final String NOME_USUARIO = "nomeusuario";
    private static final String CELULAR = "celular";
    private static final String MAIL = "mail";
    private static final String SENHA = "senha";
    private static final String DATA_NASCIMENTO = "datanascimento";
    private static final String PAIS = "pais";
    private static final String ESTADO = "estado";
    private static final String CIDADE = "cidade";
    private static final String BAIRRO = "bairro";
    private static final String RUA = "rua";
    private static final String NUMERO_CASA = "numerocasa";
    private static final String FOTO_PERFIL = "fotoperfil";

    private static final String TABELA2 = "pedidos";
    private static final String ID_PEDIDOS = "id_pedidos";
    private static final String EMAIL_HELP = "emailhelp";
    private static final String TITULO = "titulo";
    private static final String DESCRICAO = "descricao";
    private static final String FOTO_PEDIDO = "fotopedido";
    private static final String PAGAMENTO = "pagamento";
    private static final String TIPO = "tipo";

    private static final int VERSAO = 5;



    SQLiteDatabase db;

    public DataBaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+"("
                +ID+ " integer primary key autoincrement,"
                +NOME_USUARIO+ " text not null,"
                +MAIL+ " text not null,"
                +SENHA+ " text not null,"
                +CELULAR+ " text not null,"
                +DATA_NASCIMENTO+ " text,"
                +PAIS+ " text,"
                +ESTADO+ " text,"
                +CIDADE+ " text,"
                +BAIRRO+ " text,"
                +RUA+ " text,"
                +NUMERO_CASA+ " integer,"
                +FOTO_PERFIL+ " blob"
                +")";
        db.execSQL(sql);

        String sql2 = "CREATE TABLE "+TABELA2+"("
                +ID_PEDIDOS + " integer primary key autoincrement,"
                +EMAIL_HELP + " text not null,"
                +TITULO + " text not null,"
                +DESCRICAO + " text not null,"
                +FOTO_PEDIDO + " blob not null,"
                +PAGAMENTO + " text not null,"
                +TIPO + " boolean not null"
                +")";
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+TABELA2);
        onCreate(db);
    }
    public int buscarUsuario(String email, String senha) {
        db = this.getReadableDatabase();
        String query = "select mail, senha from usuarios";
        Cursor cursor = db.rawQuery(query,null);

        String bdemail, bdsenha;
        int achou = 0;
        if (cursor.moveToFirst()){
            do {
                bdemail = cursor.getString(0);
                bdsenha = cursor.getString(1);
                if (bdemail.equals(email) && bdsenha.equals(senha)){
                    //usuario e senha encontrado
                    achou = 1;
                    break;
                }
            }while (cursor.moveToNext());
        }
        //não encontrado
        return achou;
    }

    public void cadastrarbd(String strNome, String strEmail, String strSenha,byte[] array, String celular) {

        db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(NOME_USUARIO, strNome);
        values.put(MAIL, strEmail);
        values.put(SENHA, strSenha);
        values.put(CELULAR, celular);
        if (array != null){
            values.put(FOTO_PERFIL,array);
        }
        db.insert(TABELA,null,values);
        db.close();
    }
    public Cursor listarusuarios() {
        db = this.getWritableDatabase();
        Cursor dados = db.rawQuery("SELECT * FROM "+TABELA,null);
        return dados;
    }

    public void cadastrarHelp(String strTitulo, String strDesc, String strPagamento, byte[] bArray, Boolean tipo, String email) {
        db = getWritableDatabase();
        ContentValues values2 = new ContentValues();

        values2.put(EMAIL_HELP, email);
        values2.put(TITULO, strTitulo);
        values2.put(DESCRICAO, strDesc);
        values2.put(PAGAMENTO, strPagamento);
        if (bArray!=null){
            values2.put(FOTO_PEDIDO, bArray);
        }

        values2.put(TIPO, tipo);
        db.insert(TABELA2,null,values2);
        db.close();

    }

    public String buscaNome(String stremail) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT nomeusuario, mail FROM usuarios";
        Cursor cursor = db.rawQuery(selectquery, null);
        String a="Não Encontrado";
        if (cursor.moveToFirst()) {
            do{
                a = cursor.getString(1);
                if (stremail.equals(a)){
                    a = cursor.getString(0);
                    cursor.close();
                    db.close();
                    return a;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return a;

    }

    public Bitmap buscaFoto(String stremail) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT fotoperfil, mail FROM usuarios";
        Cursor cursor = db.rawQuery(selectquery, null);
        byte[] byteArray;
        String a;

        if (cursor.moveToFirst()) {
            do{
                a = cursor.getString(1);
                if (stremail.equals(a)){
                    byteArray = cursor.getBlob(0);
                    Bitmap bm = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                    cursor.close();
                    db.close();
                    return bm;
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return null;
    }

    public void listarpedidosuser(String email) {
    }

    public Cursor listarPedidosUserBD() {
        db = this.getReadableDatabase();
        String selectquery = "SELECT id_pedidos, titulo, descricao, pagamento, tipo, emailhelp FROM pedidos";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }


    }
}
