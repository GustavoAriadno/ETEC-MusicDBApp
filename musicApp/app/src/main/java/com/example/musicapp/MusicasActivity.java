package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MusicasActivity extends AppCompatActivity {

    List<Musicas> musicasList;
    MusicasAdapter musicasAdapter;
    SQLiteDatabase meuBancoDeDados;
    ListView listViewMusicas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicas_layout);

        listViewMusicas = findViewById(R.id.listarMusicas);
        musicasList = new ArrayList<>();

        meuBancoDeDados = openOrCreateDatabase(MainActivity.NOME_BANCO_DE_DADOS, MODE_PRIVATE, null);

        visualizarMusicasDatabase();
    }

    // Ira listar todas as musicas do data base
    public void visualizarMusicasDatabase() {
        Cursor cursorMusicas = meuBancoDeDados.rawQuery("SELECT * FROM musicas", null);

        if (cursorMusicas.moveToFirst()) {
            musicasList.clear();
            do {
                musicasList.add(new Musicas(
                        cursorMusicas.getInt(0),
                        cursorMusicas.getString(1),
                        cursorMusicas.getString(2),
                        cursorMusicas.getString(3),
                        cursorMusicas.getString(4)
                ));
            } while (cursorMusicas.moveToNext());
        }
        cursorMusicas.close();

        // Verificar layout
        musicasAdapter = new MusicasAdapter(this, R.layout.musica_model, musicasList, meuBancoDeDados);

        listViewMusicas.setAdapter(musicasAdapter);
    }
}