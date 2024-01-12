
package com.mycompany.filmoteca.persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.filmoteca.logica.Anio;
import com.mycompany.filmoteca.logica.Pais;
import com.mycompany.filmoteca.logica.Director;
import com.mycompany.filmoteca.logica.Genero;
import com.mycompany.filmoteca.logica.Actor;
import com.mycompany.filmoteca.logica.Pelicula;
import com.mycompany.filmoteca.persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class PeliculaJpaController implements Serializable {

    public PeliculaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public PeliculaJpaController() {
        emf = Persistence.createEntityManagerFactory("filmoteca");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pelicula pelicula) {
        if (pelicula.getListaActores() == null) {
            pelicula.setListaActores(new ArrayList<Actor>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anio anio = pelicula.getAnio();
            if (anio != null) {
                anio = em.getReference(anio.getClass(), anio.getAnio());
                pelicula.setAnio(anio);
            }
            Pais pais = pelicula.getPais();
            if (pais != null) {
                pais = em.getReference(pais.getClass(), pais.getNombre());
                pelicula.setPais(pais);
            }
            Director director = pelicula.getDirector();
            if (director != null) {
                director = em.getReference(director.getClass(), director.getId());
                pelicula.setDirector(director);
            }
            Genero genero = pelicula.getGenero();
            if (genero != null) {
                genero = em.getReference(genero.getClass(), genero.getNombre());
                pelicula.setGenero(genero);
            }
            List<Actor> attachedListaActores = new ArrayList<Actor>();
            for (Actor listaActoresActorToAttach : pelicula.getListaActores()) {
                listaActoresActorToAttach = em.getReference(listaActoresActorToAttach.getClass(), listaActoresActorToAttach.getId());
                attachedListaActores.add(listaActoresActorToAttach);
            }
            pelicula.setListaActores(attachedListaActores);
            em.persist(pelicula);
            if (anio != null) {
                anio.getListaPeliculas().add(pelicula);
                anio = em.merge(anio);
            }
            if (pais != null) {
                pais.getListaPeliculas().add(pelicula);
                pais = em.merge(pais);
            }
            if (director != null) {
                director.getListaPeliculas().add(pelicula);
                director = em.merge(director);
            }
            if (genero != null) {
                genero.getListaPeliculas().add(pelicula);
                genero = em.merge(genero);
            }
            for (Actor listaActoresActor : pelicula.getListaActores()) {
                listaActoresActor.getListaPeliculas().add(pelicula);
                listaActoresActor = em.merge(listaActoresActor);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pelicula pelicula) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pelicula persistentPelicula = em.find(Pelicula.class, pelicula.getId());
            Anio anioOld = persistentPelicula.getAnio();
            Anio anioNew = pelicula.getAnio();
            Pais paisOld = persistentPelicula.getPais();
            Pais paisNew = pelicula.getPais();
            Director directorOld = persistentPelicula.getDirector();
            Director directorNew = pelicula.getDirector();
            Genero generoOld = persistentPelicula.getGenero();
            Genero generoNew = pelicula.getGenero();
            List<Actor> listaActoresOld = persistentPelicula.getListaActores();
            List<Actor> listaActoresNew = pelicula.getListaActores();
            if (anioNew != null) {
                anioNew = em.getReference(anioNew.getClass(), anioNew.getAnio());
                pelicula.setAnio(anioNew);
            }
            if (paisNew != null) {
                paisNew = em.getReference(paisNew.getClass(), paisNew.getNombre());
                pelicula.setPais(paisNew);
            }
            if (directorNew != null) {
                directorNew = em.getReference(directorNew.getClass(), directorNew.getId());
                pelicula.setDirector(directorNew);
            }
            if (generoNew != null) {
                generoNew = em.getReference(generoNew.getClass(), generoNew.getNombre());
                pelicula.setGenero(generoNew);
            }
            List<Actor> attachedListaActoresNew = new ArrayList<Actor>();
            for (Actor listaActoresNewActorToAttach : listaActoresNew) {
                listaActoresNewActorToAttach = em.getReference(listaActoresNewActorToAttach.getClass(), listaActoresNewActorToAttach.getId());
                attachedListaActoresNew.add(listaActoresNewActorToAttach);
            }
            listaActoresNew = attachedListaActoresNew;
            pelicula.setListaActores(listaActoresNew);
            pelicula = em.merge(pelicula);
            if (anioOld != null && !anioOld.equals(anioNew)) {
                anioOld.getListaPeliculas().remove(pelicula);
                anioOld = em.merge(anioOld);
            }
            if (anioNew != null && !anioNew.equals(anioOld)) {
                anioNew.getListaPeliculas().add(pelicula);
                anioNew = em.merge(anioNew);
            }
            if (paisOld != null && !paisOld.equals(paisNew)) {
                paisOld.getListaPeliculas().remove(pelicula);
                paisOld = em.merge(paisOld);
            }
            if (paisNew != null && !paisNew.equals(paisOld)) {
                paisNew.getListaPeliculas().add(pelicula);
                paisNew = em.merge(paisNew);
            }
            if (directorOld != null && !directorOld.equals(directorNew)) {
                directorOld.getListaPeliculas().remove(pelicula);
                directorOld = em.merge(directorOld);
            }
            if (directorNew != null && !directorNew.equals(directorOld)) {
                directorNew.getListaPeliculas().add(pelicula);
                directorNew = em.merge(directorNew);
            }
            if (generoOld != null && !generoOld.equals(generoNew)) {
                generoOld.getListaPeliculas().remove(pelicula);
                generoOld = em.merge(generoOld);
            }
            if (generoNew != null && !generoNew.equals(generoOld)) {
                generoNew.getListaPeliculas().add(pelicula);
                generoNew = em.merge(generoNew);
            }
            for (Actor listaActoresOldActor : listaActoresOld) {
                if (!listaActoresNew.contains(listaActoresOldActor)) {
                    listaActoresOldActor.getListaPeliculas().remove(pelicula);
                    listaActoresOldActor = em.merge(listaActoresOldActor);
                }
            }
            for (Actor listaActoresNewActor : listaActoresNew) {
                if (!listaActoresOld.contains(listaActoresNewActor)) {
                    listaActoresNewActor.getListaPeliculas().add(pelicula);
                    listaActoresNewActor = em.merge(listaActoresNewActor);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = pelicula.getId();
                if (findPelicula(id) == null) {
                    throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(int id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pelicula pelicula;
            try {
                pelicula = em.getReference(Pelicula.class, id);
                pelicula.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pelicula with id " + id + " no longer exists.", enfe);
            }
            Anio anio = pelicula.getAnio();
            if (anio != null) {
                anio.getListaPeliculas().remove(pelicula);
                anio = em.merge(anio);
            }
            Pais pais = pelicula.getPais();
            if (pais != null) {
                pais.getListaPeliculas().remove(pelicula);
                pais = em.merge(pais);
            }
            Director director = pelicula.getDirector();
            if (director != null) {
                director.getListaPeliculas().remove(pelicula);
                director = em.merge(director);
            }
            Genero genero = pelicula.getGenero();
            if (genero != null) {
                genero.getListaPeliculas().remove(pelicula);
                genero = em.merge(genero);
            }
            List<Actor> listaActores = pelicula.getListaActores();
            for (Actor listaActoresActor : listaActores) {
                listaActoresActor.getListaPeliculas().remove(pelicula);
                listaActoresActor = em.merge(listaActoresActor);
            }
            em.remove(pelicula);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pelicula> findPeliculaEntities() {
        return findPeliculaEntities(true, -1, -1);
    }

    public List<Pelicula> findPeliculaEntities(int maxResults, int firstResult) {
        return findPeliculaEntities(false, maxResults, firstResult);
    }

    private List<Pelicula> findPeliculaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pelicula.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pelicula findPelicula(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pelicula.class, id);
        } finally {
            em.close();
        }
    }

    public int getPeliculaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pelicula> rt = cq.from(Pelicula.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
