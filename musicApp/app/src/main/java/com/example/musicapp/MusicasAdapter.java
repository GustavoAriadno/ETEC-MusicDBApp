package com.example.musicapp;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class MusicasAdapter extends ArrayAdapter<Musicas> {

    Context mCtx;
    int listaLayoutRes;
    List<Musicas> listaMuscas;
    SQLiteDatabase meuBancoDeDados;

    // Construtor do adaptador
    public MusicasAdapter(Context mCtx, int listaLayoutRes, List<Musicas> listaMuscas, SQLiteDatabase meuBancoDeDados) {
        super(mCtx, listaLayoutRes, listaMuscas);

        this.mCtx = mCtx;
        this.listaLayoutRes = listaLayoutRes;
        this.listaMuscas = listaMuscas;
        this.meuBancoDeDados = meuBancoDeDados;
    }


    // Inflar layout com MODELO e suas acoes
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(listaLayoutRes, null);

        final Musicas musicas = listaMuscas.get(position);


        TextView txtViewNome = view.findViewById(R.id.txtNomeViewMusica);
        TextView txtViewGenero = view.findViewById(R.id.txtGeneroViewMusica);
        TextView txtViewArtista = view.findViewById(R.id.txtArtistaViewMusica);
        TextView txtViewDataEntrada = view.findViewById(R.id.txtEntradaViewMusica);

        txtViewNome.setText(musicas.getNome());
        txtViewGenero.setText(musicas.getGenero());
        txtViewArtista.setText(musicas.getArtista());
        txtViewDataEntrada.setText(musicas.getDataEntrada());
        // Double to String
        // txtViewSalario.setText(String.valueOf(musicas.getDataEntrada()));

        Button btnExcluir = view.findViewById(R.id.btnExcluirViewMusica);
        Button btnEditar = view.findViewById(R.id.btnEditarViewMusica);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alterarMusica(musicas);
            }
        });
        btnExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
                builder.setTitle("Deseja excluir?");
                builder.setIcon(android.R.drawable.ic_input_delete);
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String sql = "DELETE FROM musicas WHERE id = ?";
                        meuBancoDeDados.execSQL(sql, new Integer[]{musicas.getId()});
                        recarregarMusicasDB();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Executara nada.
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        return view;
    }

    public void alterarMusica(final Musicas musicas) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.edit_music_box, null);
        builder.setView(view);

        final EditText txtEditarNome = view.findViewById(R.id.txtEditarNome);
        final EditText txtEditarArtista = view.findViewById(R.id.txtEditarArtista);
        final Spinner spnGenero = view.findViewById(R.id.spnGenero);

        txtEditarNome.setText(musicas.getNome());
        txtEditarArtista.setText(musicas.getArtista());

        final AlertDialog dialog = builder.create();
        dialog.show();

        view.findViewById(R.id.btnAlterarMusica).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = txtEditarNome.getText().toString().trim();
                String artista = txtEditarArtista.getText().toString().trim();
                String genero = spnGenero.getSelectedItem().toString().trim();

                if (nome.isEmpty()) {
                    txtEditarNome.setError("Nome esta em branco");
                    txtEditarNome.requestFocus();
                    return;
                }
                if (artista.isEmpty()) {
                    txtEditarArtista.setError("Artista esta em branco");
                    txtEditarArtista.requestFocus();
                    return;
                }

                String sql = "UPDATE musicas SET nome = ?, artista = ?, genero = ? WHERE id = ?";
                meuBancoDeDados.execSQL(sql,
                        new String[]{nome, artista, genero, String.valueOf(musicas.getId())});
                Toast.makeText(mCtx, "Musica alterada com sucesso!!!", Toast.LENGTH_LONG).show();

                // Metodo atualizador de lista
                recarregarMusicasDB();
                // Limpa estrutura do AlertDialog
                dialog.dismiss();
            }
        });
    }

    public void recarregarMusicasDB() {
        Cursor cursorMusicas = meuBancoDeDados.rawQuery("SELECT * FROM musicas", null);

        if (cursorMusicas.moveToFirst()) {
            listaMuscas.clear();
            do {
                listaMuscas.add(new Musicas(
                        cursorMusicas.getInt(0),
                        cursorMusicas.getString(1),
                        cursorMusicas.getString(2),
                        cursorMusicas.getString(3),
                        cursorMusicas.getString(4)
                ));
            } while (cursorMusicas.moveToNext());
        }
        cursorMusicas.close();
        notifyDataSetChanged();
    }
}
