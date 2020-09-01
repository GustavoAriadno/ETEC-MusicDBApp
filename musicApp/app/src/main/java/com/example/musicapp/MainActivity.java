package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Variaveis globais
    public static final String NOME_BANCO_DE_DADOS = "musicas.db";
    EditText txtNovoNome, txtNovoArtista;
    Button btnAdicionarMusica, btnVisualizarMusicas;
    Spinner spngeneros;
    SQLiteDatabase meuBancoDeDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtNovoNome = findViewById(R.id.txtNovoNome);
        txtNovoArtista = findViewById(R.id.txtNovoArtista);
        spngeneros = findViewById(R.id.spngenero);

        btnAdicionarMusica = findViewById(R.id.btnAdicionarMusica);
        btnAdicionarMusica.setOnClickListener(this);

        btnVisualizarMusicas = findViewById(R.id.btnVisualizarMusicas);
        btnVisualizarMusicas.setOnClickListener(this);

        // Criando DB
        meuBancoDeDados = openOrCreateDatabase(NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        // Colorindo Spinner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.generos,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spngeneros.setAdapter(adapter);

        // Criar tabela
        criarTabelaMusica();
    }

    // Este método irá criar a tabela
    // como vamos chamar esse método toda vez que lançarmos o aplicativo
    // Entao, adicionei IF NOT EXISTS ao SQL
    private void criarTabelaMusica() {
        String sql = "CREATE TABLE IF NOT EXISTS musicas(" +
                "id integer PRIMARY KEY AUTOINCREMENT, " +
                "nome varchar(200) NOT NULL, " +
                "artista varchar(200) NOT NULL, " +
                "genero varchar(200) NOT NULL, " +
                "dataEntrada datetime NOT NULL);";
        meuBancoDeDados.execSQL(sql);
    }

    private boolean verificarEntrada(String nome, String artista) {
        if (nome.isEmpty()) {
            txtNovoNome.setError("Por favor, digite o nome da musica");
            txtNovoNome.requestFocus();
            return false;
        }
        if (artista.isEmpty()) {
            txtNovoArtista.setError("Por favor, digite o nome do artista");
            txtNovoArtista.requestFocus();
            return false;
        }
        return true;
    }

    public void adicionarMusica() {
        String nomeMsc = txtNovoNome.getText().toString().trim();
        String artistaMsc = txtNovoArtista.getText().toString().trim();
        String generoMsc = spngeneros.getSelectedItem().toString();

        // Obtendo o horário atual para data de inclusão
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        String dataEntrada = simpleDateFormat.format(calendar.getTime());

        // Validando Entrada
        if (verificarEntrada(nomeMsc, artistaMsc)) {
            String insertSql = "INSERT INTO musicas (" +
                    "nome, " +
                    "artista, " +
                    "genero, " +
                    "dataEntrada)" +
                    "VALUES (?, ?, ?, ?);";
            // usando o mesmo método execsql para inserir valores
            // desta vez tem dois parâmetros
            // primeiro é a string sql e segundo são os parâmetros que devem ser vinculados à consulta
            meuBancoDeDados.execSQL(insertSql, new String[]{nomeMsc, artistaMsc, generoMsc, dataEntrada});
            Toast.makeText(getApplicationContext(), "Musica adicionada com sucesso!!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAdicionarMusica:
                adicionarMusica();
                break;
            case R.id.btnVisualizarMusicas:
                startActivity(new Intent(getApplicationContext(), MusicasActivity.class));
                break;
        }

    }

}