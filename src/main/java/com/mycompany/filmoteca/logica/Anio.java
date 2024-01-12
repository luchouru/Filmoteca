
package com.mycompany.filmoteca.logica;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Anio implements Serializable {
    @Id
    private int anio;   
    @OneToMany(mappedBy = "anio")
    private List<Pelicula> listaPeliculas;

    public Anio() {
    }

    public Anio(int anio, List<Pelicula> listaPeliculas) {
        this.anio = anio;
        this.listaPeliculas = listaPeliculas;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public List<Pelicula> getListaPeliculas() {
        return listaPeliculas;
    }

    public void setListaPeliculas(List<Pelicula> listaPeliculas) {
        this.listaPeliculas = listaPeliculas;
    }
    
    
    
}
