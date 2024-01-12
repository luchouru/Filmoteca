package com.mycompany.filmoteca.logica;

import com.mycompany.filmoteca.logica.Pelicula;
import javax.annotation.processing.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="org.eclipse.persistence.internal.jpa.modelgen.CanonicalModelProcessor", date="2024-01-08T17:09:35", comments="EclipseLink-2.7.10.v20211216-rNA")
@StaticMetamodel(Actor.class)
public class Actor_ { 

    public static volatile ListAttribute<Actor, Pelicula> listaPeliculas;
    public static volatile SingularAttribute<Actor, Integer> id;
    public static volatile SingularAttribute<Actor, String> nombre;

}