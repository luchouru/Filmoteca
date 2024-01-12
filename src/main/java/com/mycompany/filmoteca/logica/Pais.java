package com.mycompany.filmoteca.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Pais implements Serializable {
    @Id
    private String nombre;
    @OneToMany(mappedBy = "nacionalidad")
    private List<Director> listaDirectores;
    @OneToMany(mappedBy = "pais")
    private List<Pelicula> listaPeliculas;

    public Pais() {
    }

    public Pais(String nombre, List<Director> listaDirectores, List<Pelicula> listaPeliculas) {
        this.nombre = nombre;
        this.listaDirectores = listaDirectores;
        this.listaPeliculas = listaPeliculas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Director> getListaDirectores() {
        return listaDirectores;
    }

    public void setListaDirectores(List<Director> listaDirectores) {
        this.listaDirectores = listaDirectores;
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }
    
    
}
