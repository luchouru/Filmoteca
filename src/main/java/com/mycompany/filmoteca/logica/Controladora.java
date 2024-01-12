
package com.mycompany.filmoteca.logica;

import com.mycompany.filmoteca.persistencia.ControladoraPersistencia;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class Controladora {
    
    ControladoraPersistencia controlPersis = new ControladoraPersistencia();

    public List<Anio> traerAnios() {
        return controlPersis.traerAniosBD();
    }

    public List<Genero> traerGeneros() {
        return controlPersis.traerGenerosBD();
    }

    public List<Pais> traerPaises() {
        return controlPersis.traerPaisesBD();
    }

    public List<Director> traerDirectores() {
        List<Director> listaDirectores = controlPersis.traerDirectoresBD();
        Comparator<Director> comparadorNombre = Comparator.comparing(Director::getNombre);
        Collections.sort(listaDirectores, comparadorNombre);
        
        return listaDirectores;
    }

    public void agregarDirector(String nombre, String pais) {
        Director nuevo = new Director();
        nuevo.setNombre(nombre);
        Pais nacionalidad = controlPersis.encontrarPaisBD(pais);
        nuevo.setNacionalidad(nacionalidad);
        controlPersis.agregarDirectorBD(nuevo);
    }

    public void agregarPelicula(String nombre, int anio, String director, String pais, 
            String genero, int calificacion) {
        Pelicula nueva = new Pelicula();
        nueva.setNombre(nombre);
        Anio a = controlPersis.encontrarAnioBD(anio);
        nueva.setAnio(a);
        Pais nacionalidad = controlPersis.encontrarPaisBD(pais);
        nueva.setPais(nacionalidad);
        Genero g = controlPersis.encontrarGeneroBD(genero);
        nueva.setGenero(g);
        nueva.setCalificacion(calificacion);
        Director d = this.encontrarDirector(director);
        nueva.setDirector(d);
        
        controlPersis.agregarPeliculaBD(nueva);
    }

    private Director encontrarDirector(String director) {
        List<Director> listaDirectores = controlPersis.traerDirectoresBD();
        if(listaDirectores != null){
            for(Director actual : listaDirectores){
                if(actual.getNombre().equals(director))
                    return actual;
            }
        }
        return null;
    }

    public List<Pelicula> traerFilmografia(String director) {
        Director encontrado = this.encontrarDirector(director);
        List<Pelicula> listaPeliculas = encontrado.getListaPeliculas();
        Comparator<Pelicula> compararAnio = Comparator.comparing(Pelicula::getAnioInteger);
        Collections.sort(listaPeliculas, compararAnio);
  
        return encontrado.getListaPeliculas();
    }

    public List<Pelicula> traerPeliculasDelAnio(int anio) {
        Anio encontrado = controlPersis.encontrarAnioBD(anio);
        List<Pelicula> listaPeliculas = encontrado.getListaPeliculas();
        
        Comparator<Pelicula> comparadorCalificacion = Comparator.comparingInt(Pelicula::getCalificacion);        
        Collections.sort(listaPeliculas, comparadorCalificacion.reversed());
        
        return listaPeliculas; 
    }

    public List<Actor> traerActores() {
        return controlPersis.TraerActoresBD();
    }

    public void agregarActor(String nombre) {
        Actor nuevo = new Actor();
        nuevo.setNombre(nombre);
        controlPersis.agregarActorBD(nuevo);
    }

    public List<Pelicula> traerPeliculas() {
        List<Pelicula> listaPeliculas = controlPersis.traerPeliculasBD();
        Comparator<Pelicula> comparadorCalificacion = Comparator.comparing(Pelicula::getNombre);
        Collections.sort(listaPeliculas, comparadorCalificacion);
        return listaPeliculas;
    }

    public Actor traerActor(int id) {
        return controlPersis.encontrarActorBD(id);
    }

    public void agregarReparto(Actor actor, int idPelicula) {
        Pelicula pelicula = controlPersis.encontrarPelicula(idPelicula);
        List<Actor> reparto = pelicula.getListaActores();
        reparto.add(actor);
        pelicula.setListaActores(reparto);
        
        List<Pelicula> filmografia = actor.getListaPeliculas();
        filmografia.add(pelicula);
        actor.setListaPeliculas(filmografia);
        
        controlPersis.modificarRepartoBD(pelicula,actor);
    }

    public Pelicula encontrarPelicula(int id) {
        return controlPersis.encontrarPeliculaBD(id);
    }

    public List<Actor> traerRepartoPelicula(int id) {
        Pelicula pelicula = controlPersis.encontrarPeliculaBD(id);
        List<Actor> reparto = pelicula.getListaActores();
        Comparator<Actor> comparaNombre = Comparator.comparing(Actor::getNombre);
        Collections.sort(reparto, comparaNombre);
        return reparto;
    }

    public Director encontrarDirector(int id) {
        return controlPersis.encontrarDirectorBD(id);
    }

    public List<Pelicula> traerFilmografia(int id) {
        Director director = controlPersis.encontrarDirectorBD(id);
        List<Pelicula> filmografia = director.getListaPeliculas();
        Comparator<Pelicula> compararAnio = Comparator.comparingInt(Pelicula::getAnioInteger);
        Collections.sort(filmografia, compararAnio);
        return filmografia;
    }

    public List<Pelicula> traerFilmografiaActor(int id) {
        Actor actor = controlPersis.encontrarActorBD(id);
        List<Pelicula> filmografia = actor.getListaPeliculas();
        Comparator<Pelicula> compararAnio = Comparator.comparingInt(Pelicula::getAnioInteger);
        Collections.sort(filmografia, compararAnio);
        return filmografia;
    }

    public List<Director> traerDirectoresPais(String pais) {
        Pais nacionalidad = controlPersis.encontrarPaisBD(pais);
        List<Director> listaDirectores = nacionalidad.getListaDirectores();
        Comparator<Director> compararNombre = Comparator.comparing(Director::getNombre);
        Collections.sort(listaDirectores, compararNombre);
        return listaDirectores;
    }

    public List<Pelicula> traerPeliculasDelGenero(String genero) {
        Genero gen = controlPersis.encontrarGeneroBD(genero);
        List<Pelicula> listaPeliculas = gen.getListaPeliculas();
        Comparator<Pelicula> compararAnio = Comparator.comparingInt(Pelicula::getAnioInteger);
        Collections.sort(listaPeliculas, compararAnio);
        return listaPeliculas;
    }

    public List<Pelicula> traerPeliculasDelPais(String pais) {
        Pais nacionalidad = controlPersis.encontrarPaisBD(pais);
        List<Pelicula> listaPeliculas = nacionalidad.getListaPeliculas();
        Comparator<Pelicula> compararAnio = Comparator.comparingInt(Pelicula::getAnioInteger);
        Collections.sort(listaPeliculas, compararAnio);
        return listaPeliculas;
    }

    


}
