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
    private static final String CEP = "cep";
    private static final String COMPLEMENTO = "complemento";
    private static final String GENERO = "genero";

    private static final String TABELA2 = "pedidos";
    private static final String ID_PEDIDOS = "id_pedidos";
    private static final String EMAIL_HELP = "emailhelp";
    private static final String TITULO = "titulo";
    private static final String DESCRICAO = "descricao";
    private static final String FOTO_PEDIDO = "fotopedido";
    private static final String PAGAMENTO = "pagamento";
    private static final String TIPO = "tipo";

    private static final String TABELA3 = "checagempedidos";
    private static final String ID_CHECAGEM = "idchecagem";
    private static final String ID_USUER_CHECk = "idusercheck";
    private static final String ID_PEDIDO_ACEITO = "pedidoaceito";
    private static final String ID_PEDIDO_RECUSADO = "pedidorecusado";

    private static final int VERSAO = 7;



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
                +NUMERO_CASA+ " text,"
                +CEP+ " text,"
                +COMPLEMENTO+ " text,"
                +GENERO+ " text,"
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

        String sql3 = "CREATE TABLE "+TABELA3+"("
                +ID_CHECAGEM + " integer primary key autoincrement,"
                +ID_USUER_CHECk + " integer not null,"
                +ID_PEDIDO_ACEITO + " integer,"
                +ID_PEDIDO_RECUSADO + " integer"
                +")";
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABELA);
        db.execSQL("DROP TABLE IF EXISTS "+TABELA2);
        db.execSQL("DROP TABLE IF EXISTS "+TABELA3);
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
        values.put(PAIS,"Brasil");
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
                    if (cursor.getBlob(0) != null){
                        byteArray = cursor.getBlob(0);
                        Bitmap bm = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                        cursor.close();
                        db.close();
                        return bm;
                    }

                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return null;
    }

    public void listarpedidosuser(String email) {
    }

    public Cursor listarPedidosUserBD(String email) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT id_pedidos, titulo, descricao, pagamento, tipo, emailhelp FROM pedidos WHERE emailhelp = ?";
        Cursor cursor = db.rawQuery(selectquery,new String[]{email});
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }


    }

    public void alterarCadastro(String strNome, String strSenha, String strNascimento, String strCEP, String strBairro,
                                String strEstado, String strCidade, String strRua, String strNumero, String strComplemento,
                                byte[] bArray, String email, String strSexo) {
        db = this.getWritableDatabase();
        String where;

        where = MAIL + "= '" + email +"'";
        ContentValues values =  new ContentValues();

        values.put(NOME_USUARIO,strNome);
        values.put(SENHA,strSenha);
        values.put(DATA_NASCIMENTO,strNascimento);
        values.put(CEP,strCEP);
        values.put(BAIRRO,strBairro);
        values.put(ESTADO,strEstado);
        values.put(CIDADE,strCidade);
        values.put(RUA,strRua);
        values.put(NUMERO_CASA,strNumero);
        values.put(COMPLEMENTO,strComplemento);
        if (bArray != null){
            values.put(FOTO_PERFIL,bArray);
        }
        values.put(GENERO,strSexo);
        db.update(TABELA,values,where,null);

    }

    public Cursor preencherPerfil(String email) {
        db = this.getReadableDatabase();

        String selectquery = "SELECT nomeusuario, datanascimento, genero, cidade, estado," +
                " rua, bairro, numerocasa, cep, complemento FROM usuarios WHERE mail = ?";
        Cursor cursor = db.rawQuery(selectquery,new String[]{email});

        if (cursor.getCount()>0){
            return cursor;
        }else{
            return null;
        }


    }

    public Cursor listarPedidosBD() {
        db = this.getReadableDatabase();
        String selectquery = "SELECT id_pedidos, titulo, descricao, fotopedido, pagamento, tipo  FROM pedidos";
        Cursor cursor = db.rawQuery(selectquery, null);
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }
    }

    public int verificaSelf(int id_pedido, String email) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT emailhelp FROM pedidos WHERE id_pedidos = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{String.valueOf(id_pedido)});
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            String mailhelp = cursor.getString(0);
            if (mailhelp.equals(email)){
                //retorne é da mesma pessoa
                return 0;
            }else {
                //pode proseguir
                return 1;
            }
        }
        return 2; ///aqui fudeu a busca
    }

    public int buscaIdUsuario(String email) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT id FROM usuarios WHERE mail = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{email});
        cursor.moveToFirst();
        int id_user;
        cursor.moveToFirst();
        if (cursor.getCount()>0){
            id_user  = cursor.getInt(0);
            return id_user;
        }
        else {
            return 0;
        }

    }

    public void registrarPedidoAceito(int id_user, int id_pedido) {
        db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();

        values.put(ID_USUER_CHECk,id_user);
        values.put(ID_PEDIDO_ACEITO,id_pedido);
        db.insert(TABELA3,null,values);
    }
    public void registrarPedidoRecusado(int id_user, int id_pedido) {
        db = this.getWritableDatabase();
        ContentValues values =  new ContentValues();

        values.put(ID_USUER_CHECk,id_user);
        values.put(ID_PEDIDO_RECUSADO,id_pedido);
        db.insert(TABELA3,null,values);
    }

    public Cursor dependenciasDoUsuario(int id_user) {
        db = this.getReadableDatabase();
        //String id = Integer.toString(id_user);
        String selectquery = "SELECT pedidoaceito, pedidorecusado FROM checagempedidos WHERE idusercheck = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{String.valueOf(id_user)});
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }
    }

    public int buscaTelefone(String strCelular) {
        db = this.getReadableDatabase();
        String query = "select celular from usuarios";
        Cursor cursor = db.rawQuery(query,null);
        String celularesBD;
        int achou = 0;
        if (cursor.moveToFirst()){
            do {
                celularesBD = cursor.getString(0);
                if (celularesBD.equals(strCelular)){
                    //celular já cadastrado
                    achou = 1;
                    break;
                }
            }while (cursor.moveToNext());
        }
        //não encontrado
        return achou;
    }

    public Cursor listarPedidoParaAlterar(int id) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT titulo, descricao, fotopedido, pagamento, tipo, emailhelp  FROM pedidos WHERE id_pedidos = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{String.valueOf(id)});
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }

    }

    public void alterarHelp(String strTitulo, String strDesc, String strPagamento, byte[] bArray, Boolean tipo, int idd) {
        db = this.getWritableDatabase();

        String where;

        where = ID_PEDIDOS + "=" + idd;
        ContentValues values;

        values = new ContentValues();
        values.put(TITULO,strTitulo);
        values.put(DESCRICAO,strDesc);
        values.put(PAGAMENTO,strPagamento);
        values.put(FOTO_PEDIDO,bArray);
        values.put(TIPO,tipo);

        db.update(TABELA2,values,where,null);


    }

    public Cursor listarpedidosAceitos(int id_usuario) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT pedidoaceito FROM checagempedidos WHERE idusercheck = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{String.valueOf(id_usuario)});
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }

    }

    public Cursor buscaDadosdoPedido(int id_pedido_aceito) {

        db = this.getReadableDatabase();
        String selectquery = "SELECT emailhelp, titulo, pagamento FROM pedidos WHERE id_pedidos = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{String.valueOf(id_pedido_aceito)});
        if (cursor.getCount()>0){
            return cursor;
        }
        else {
            return null;
        }
    }

    public String buscarCelular(String email_criador_pedido) {
        db = this.getReadableDatabase();
        String selectquery = "SELECT celular FROM usuarios WHERE mail = ?";
        Cursor cursor = db.rawQuery(selectquery, new String[]{String.valueOf(email_criador_pedido)});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            return cursor.getString(0);
        }
        else {
            return null;
        }
    }

    public void removerPedido(int id) {
        String where = ID_PEDIDOS + "=" + id;
        db = this.getReadableDatabase();
        db.delete(TABELA2,where,null);
    }
}
