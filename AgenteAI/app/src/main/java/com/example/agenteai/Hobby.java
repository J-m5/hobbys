package com.example.agenteai;

public class Hobby {
    private int id;
    private String nombre;
    private String nivelDificultad;
    private String fechaRegistro;

    // Constructor vacío
    public Hobby() {}

    // Constructor completo
    public Hobby(int id, String nombre, String nivelDificultad, String fechaRegistro) {
        this.id = id;
        this.nombre = nombre;
        this.nivelDificultad = nivelDificultad;
        this.fechaRegistro = fechaRegistro;
    }

    // Constructor sin ID (para insertar nuevos registros)
    public Hobby(String nombre, String nivelDificultad, String fechaRegistro) {
        this.nombre = nombre;
        this.nivelDificultad = nivelDificultad;
        this.fechaRegistro = fechaRegistro;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Hobby{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", nivelDificultad='" + nivelDificultad + '\'' +
                ", fechaRegistro='" + fechaRegistro + '\'' +
                '}';
    }
}
