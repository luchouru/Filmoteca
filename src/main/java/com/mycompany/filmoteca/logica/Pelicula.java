package com.mycompany.filmoteca.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Pelicula implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String nombre;
    @ManyToOne
    @JoinColumn(name = "fk_anio")
    private Anio anio;
    @ManyToOne
    @JoinColumn(name = "fk_pais")
    private Pais pais;
    @ManyToOne
    @JoinColumn(name = "fk_director")
    private Director director;
    @ManyToOne
    @JoinColumn(name = "fk_genero")
    private Genero genero;
    private int calificacion;
    @ManyToMany(mappedBy = "listaPeliculas")
    private List<Actor> listaActores;

    public Pelicula() {
    }

    public Pelicula(int id, String nombre, Anio anio, Pais pais, Director director, 
            Genero genero, int calificacion, List<Actor> listaActores) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
        this.pais = pais;
        this.director = director;
        this.genero = genero;
        this.calificacion = calificacion;
        this.listaActores = listaActores;
    }

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

    public Anio getAnio() {
        return anio;
    }

    public void setAnio(Anio anio) {
        this.anio = anio;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Director getDirector() {
        return director;
    }

    public void setDirector(Director director) {
        this.director = director;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public List<Actor> getListaActores() {
        return listaActores;
    }

    public void setListaActores(List<Actor> listaActores) {
        this.listaActores = listaActores;
    }
    
    public int getAnioInteger(){
        return anio.getAnio();
    }
    
    
}
