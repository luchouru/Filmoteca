package com.mycompany.filmoteca.logica;

import com.mycompany.filmoteca.logica.Actor;
import com.mycompany.filmoteca.logica.Anio;
import com.mycompany.filmoteca.logica.Director;
import com.mycompany.filmoteca.logica.Genero;
import com.mycompany.filmoteca.logica.Pais;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-01-08T17:09:35", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Pelicula.class)
public class Pelicula_ { 

    public static volatile SingularAttribute<Pelicula, Integer> calificacion;
    public static volatile SingularAttribute<Pelicula, Director> director;
    public static volatile SingularAttribute<Pelicula, Genero> genero;
    public static volatile SingularAttribute<Pelicula, Integer> id;
    public static volatile ListAttribute<Pelicula, Actor> listaActores;
    public static volatile SingularAttribute<Pelicula, String> nombre;
    public static volatile SingularAttribute<Pelicula, Anio> anio;
    public static volatile SingularAttribute<Pelicula, Pais> pais;

}