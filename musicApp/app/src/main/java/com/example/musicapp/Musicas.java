package com.example.musicapp;

public class Musicas {
    int id;
    String nome, artista, genero, dataEntrada;

    // Construtor vazio
    public Musicas() {
    }

    // Construtor
    public Musicas(int id, String nome, String artista, String genero, String dataEntrada) {
        this.id = id;
        this.nome = nome;
        this.artista = artista;
        this.genero = genero;
        this.dataEntrada = dataEntrada;
    }
    // Getters and Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getArtista() { return artista; }

    public void setArtista(String artista) { this.artista = artista; }

    public String getGenero() { return genero; }

    public void setGenero(String genero) { this.genero = genero; }

    public String getDataEntrada() { return dataEntrada; }

    public void setDataEntrada(String dataEntrada) { this.dataEntrada = dataEntrada; }
}
