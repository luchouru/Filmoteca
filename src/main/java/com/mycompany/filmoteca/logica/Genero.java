package com.mycompany.filmoteca.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Genero implements Serializable {
    @Id
    private String nombre;
    @OneToMany(mappedBy = "genero")
    private List<Pelicula> listaPeliculas;

    public Genero() {
    }

    public Genero(String nombre, List<Pelicula> listaPeliculas) {
        this.nombre = nombre;
        this.listaPeliculas = listaPeliculas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }
    
    
    
}
