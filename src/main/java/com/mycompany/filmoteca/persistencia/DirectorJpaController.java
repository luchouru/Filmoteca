
package com.mycompany.filmoteca.persistencia;

import com.mycompany.filmoteca.logica.Director;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.filmoteca.logica.Pais;
import com.mycompany.filmoteca.logica.Pelicula;
import com.mycompany.filmoteca.persistencia.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class DirectorJpaController implements Serializable {

    public DirectorJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public DirectorJpaController() {
        emf = Persistence.createEntityManagerFactory("filmoteca");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Director director) {
        if (director.getListaPeliculas() == null) {
            director.setListaPeliculas(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais nacionalidad = director.getNacionalidad();
            if (nacionalidad != null) {
                nacionalidad = em.getReference(nacionalidad.getClass(), nacionalidad.getNombre());
                director.setNacionalidad(nacionalidad);
            }
            List<Pelicula> attachedListaPeliculas = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasPeliculaToAttach : director.getListaPeliculas()) {
                listaPeliculasPeliculaToAttach = em.getReference(listaPeliculasPeliculaToAttach.getClass(), listaPeliculasPeliculaToAttach.getId());
                attachedListaPeliculas.add(listaPeliculasPeliculaToAttach);
            }
            director.setListaPeliculas(attachedListaPeliculas);
            em.persist(director);
            if (nacionalidad != null) {
                nacionalidad.getListaDirectores().add(director);
                nacionalidad = em.merge(nacionalidad);
            }
            for (Pelicula listaPeliculasPelicula : director.getListaPeliculas()) {
                Director oldDirectorOfListaPeliculasPelicula = listaPeliculasPelicula.getDirector();
                listaPeliculasPelicula.setDirector(director);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
                if (oldDirectorOfListaPeliculasPelicula != null) {
                    oldDirectorOfListaPeliculasPelicula.getListaPeliculas().remove(listaPeliculasPelicula);
                    oldDirectorOfListaPeliculasPelicula = em.merge(oldDirectorOfListaPeliculasPelicula);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Director director) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Director persistentDirector = em.find(Director.class, director.getId());
            Pais nacionalidadOld = persistentDirector.getNacionalidad();
            Pais nacionalidadNew = director.getNacionalidad();
            List<Pelicula> listaPeliculasOld = persistentDirector.getListaPeliculas();
            List<Pelicula> listaPeliculasNew = director.getListaPeliculas();
            if (nacionalidadNew != null) {
                nacionalidadNew = em.getReference(nacionalidadNew.getClass(), nacionalidadNew.getNombre());
                director.setNacionalidad(nacionalidadNew);
            }
            List<Pelicula> attachedListaPeliculasNew = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasNewPeliculaToAttach : listaPeliculasNew) {
                listaPeliculasNewPeliculaToAttach = em.getReference(listaPeliculasNewPeliculaToAttach.getClass(), listaPeliculasNewPeliculaToAttach.getId());
                attachedListaPeliculasNew.add(listaPeliculasNewPeliculaToAttach);
            }
            listaPeliculasNew = attachedListaPeliculasNew;
            director.setListaPeliculas(listaPeliculasNew);
            director = em.merge(director);
            if (nacionalidadOld != null && !nacionalidadOld.equals(nacionalidadNew)) {
                nacionalidadOld.getListaDirectores().remove(director);
                nacionalidadOld = em.merge(nacionalidadOld);
            }
            if (nacionalidadNew != null && !nacionalidadNew.equals(nacionalidadOld)) {
                nacionalidadNew.getListaDirectores().add(director);
                nacionalidadNew = em.merge(nacionalidadNew);
            }
            for (Pelicula listaPeliculasOldPelicula : listaPeliculasOld) {
                if (!listaPeliculasNew.contains(listaPeliculasOldPelicula)) {
                    listaPeliculasOldPelicula.setDirector(null);
                    listaPeliculasOldPelicula = em.merge(listaPeliculasOldPelicula);
                }
            }
            for (Pelicula listaPeliculasNewPelicula : listaPeliculasNew) {
                if (!listaPeliculasOld.contains(listaPeliculasNewPelicula)) {
                    Director oldDirectorOfListaPeliculasNewPelicula = listaPeliculasNewPelicula.getDirector();
                    listaPeliculasNewPelicula.setDirector(director);
                    listaPeliculasNewPelicula = em.merge(listaPeliculasNewPelicula);
                    if (oldDirectorOfListaPeliculasNewPelicula != null && !oldDirectorOfListaPeliculasNewPelicula.equals(director)) {
                        oldDirectorOfListaPeliculasNewPelicula.getListaPeliculas().remove(listaPeliculasNewPelicula);
                        oldDirectorOfListaPeliculasNewPelicula = em.merge(oldDirectorOfListaPeliculasNewPelicula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = director.getId();
                if (findDirector(id) == null) {
                    throw new NonexistentEntityException("The director with id " + id + " no longer exists.");
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
            Director director;
            try {
                director = em.getReference(Director.class, id);
                director.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The director with id " + id + " no longer exists.", enfe);
            }
            Pais nacionalidad = director.getNacionalidad();
            if (nacionalidad != null) {
                nacionalidad.getListaDirectores().remove(director);
                nacionalidad = em.merge(nacionalidad);
            }
            List<Pelicula> listaPeliculas = director.getListaPeliculas();
            for (Pelicula listaPeliculasPelicula : listaPeliculas) {
                listaPeliculasPelicula.setDirector(null);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
            }
            em.remove(director);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Director> findDirectorEntities() {
        return findDirectorEntities(true, -1, -1);
    }

    public List<Director> findDirectorEntities(int maxResults, int firstResult) {
        return findDirectorEntities(false, maxResults, firstResult);
    }

    private List<Director> findDirectorEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Director.class));
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

    public Director findDirector(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Director.class, id);
        } finally {
            em.close();
        }
    }

    public int getDirectorCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Director> rt = cq.from(Director.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
