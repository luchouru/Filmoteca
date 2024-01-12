
package com.mycompany.filmoteca.persistencia;

import com.mycompany.filmoteca.logica.Actor;
import com.mycompany.filmoteca.logica.Anio;
import com.mycompany.filmoteca.logica.Director;
import com.mycompany.filmoteca.logica.Genero;
import com.mycompany.filmoteca.logica.Pais;
import com.mycompany.filmoteca.logica.Pelicula;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ControladoraPersistencia {
    
    ActorJpaController actorController = new ActorJpaController();
    AnioJpaController anioController = new AnioJpaController();
    DirectorJpaController directorController = new DirectorJpaController();
    GeneroJpaController generoController = new GeneroJpaController();
    PaisJpaController paisController = new PaisJpaController();
    PeliculaJpaController peliculaController = new PeliculaJpaController();


    public List<Anio> traerAniosBD() {
        return anioController.findAnioEntities();
    }

    public List<Genero> traerGenerosBD() {
        return generoController.findGeneroEntities();
    }

    public List<Pais> traerPaisesBD() {
        return paisController.findPaisEntities();
    }

    public List<Director> traerDirectoresBD() {
        return directorController.findDirectorEntities();
    }

    public Pais encontrarPaisBD(String pais) {
        return paisController.findPais(pais);
    }

    public void agregarDirectorBD(Director nuevo) {
        directorController.create(nuevo);
    }

    public Anio encontrarAnioBD(int anio) {
        return anioController.findAnio(anio);
    }

    public Genero encontrarGeneroBD(String genero) {
        return generoController.findGenero(genero);
    }

    public void agregarPeliculaBD(Pelicula nueva) {
        peliculaController.create(nueva);
    }

    public List<Actor> TraerActoresBD() {
        return actorController.findActorEntities();
    }

    public void agregarActorBD(Actor nuevo) {
        actorController.create(nuevo);
    }

    public List<Pelicula> traerPeliculasBD() {
        return peliculaController.findPeliculaEntities();
    }

    public Actor encontrarActorBD(int id) {
        return actorController.findActor(id);
    }

    public Pelicula encontrarPelicula(int idPelicula) {
        return peliculaController.findPelicula(idPelicula);
    }

    public void modificarRepartoBD(Pelicula pelicula, Actor actor) {
        try {
            peliculaController.edit(pelicula);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
            actorController.edit(actor);
        } catch (Exception ex) {
            Logger.getLogger(ControladoraPersistencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    public Pelicula encontrarPeliculaBD(int id) {
        return peliculaController.findPelicula(id);
    }

    public Director encontrarDirectorBD(int id) {
        return directorController.findDirector(id);
    }


    
}
