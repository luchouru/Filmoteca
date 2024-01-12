
package com.mycompany.filmoteca.persistencia;

import com.mycompany.filmoteca.logica.Genero;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.filmoteca.logica.Pelicula;
import com.mycompany.filmoteca.persistencia.exceptions.NonexistentEntityException;
import com.mycompany.filmoteca.persistencia.exceptions.PreexistingEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class GeneroJpaController implements Serializable {

    public GeneroJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public GeneroJpaController() {
        emf = Persistence.createEntityManagerFactory("filmoteca");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Genero genero) throws PreexistingEntityException, Exception {
        if (genero.getListaPeliculas() == null) {
            genero.setListaPeliculas(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pelicula> attachedListaPeliculas = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasPeliculaToAttach : genero.getListaPeliculas()) {
                listaPeliculasPeliculaToAttach = em.getReference(listaPeliculasPeliculaToAttach.getClass(), listaPeliculasPeliculaToAttach.getId());
                attachedListaPeliculas.add(listaPeliculasPeliculaToAttach);
            }
            genero.setListaPeliculas(attachedListaPeliculas);
            em.persist(genero);
            for (Pelicula listaPeliculasPelicula : genero.getListaPeliculas()) {
                Genero oldGeneroOfListaPeliculasPelicula = listaPeliculasPelicula.getGenero();
                listaPeliculasPelicula.setGenero(genero);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
                if (oldGeneroOfListaPeliculasPelicula != null) {
                    oldGeneroOfListaPeliculasPelicula.getListaPeliculas().remove(listaPeliculasPelicula);
                    oldGeneroOfListaPeliculasPelicula = em.merge(oldGeneroOfListaPeliculasPelicula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findGenero(genero.getNombre()) != null) {
                throw new PreexistingEntityException("Genero " + genero + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Genero genero) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero persistentGenero = em.find(Genero.class, genero.getNombre());
            List<Pelicula> listaPeliculasOld = persistentGenero.getListaPeliculas();
            List<Pelicula> listaPeliculasNew = genero.getListaPeliculas();
            List<Pelicula> attachedListaPeliculasNew = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasNewPeliculaToAttach : listaPeliculasNew) {
                listaPeliculasNewPeliculaToAttach = em.getReference(listaPeliculasNewPeliculaToAttach.getClass(), listaPeliculasNewPeliculaToAttach.getId());
                attachedListaPeliculasNew.add(listaPeliculasNewPeliculaToAttach);
            }
            listaPeliculasNew = attachedListaPeliculasNew;
            genero.setListaPeliculas(listaPeliculasNew);
            genero = em.merge(genero);
            for (Pelicula listaPeliculasOldPelicula : listaPeliculasOld) {
                if (!listaPeliculasNew.contains(listaPeliculasOldPelicula)) {
                    listaPeliculasOldPelicula.setGenero(null);
                    listaPeliculasOldPelicula = em.merge(listaPeliculasOldPelicula);
                }
            }
            for (Pelicula listaPeliculasNewPelicula : listaPeliculasNew) {
                if (!listaPeliculasOld.contains(listaPeliculasNewPelicula)) {
                    Genero oldGeneroOfListaPeliculasNewPelicula = listaPeliculasNewPelicula.getGenero();
                    listaPeliculasNewPelicula.setGenero(genero);
                    listaPeliculasNewPelicula = em.merge(listaPeliculasNewPelicula);
                    if (oldGeneroOfListaPeliculasNewPelicula != null && !oldGeneroOfListaPeliculasNewPelicula.equals(genero)) {
                        oldGeneroOfListaPeliculasNewPelicula.getListaPeliculas().remove(listaPeliculasNewPelicula);
                        oldGeneroOfListaPeliculasNewPelicula = em.merge(oldGeneroOfListaPeliculasNewPelicula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = genero.getNombre();
                if (findGenero(id) == null) {
                    throw new NonexistentEntityException("The genero with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Genero genero;
            try {
                genero = em.getReference(Genero.class, id);
                genero.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The genero with id " + id + " no longer exists.", enfe);
            }
            List<Pelicula> listaPeliculas = genero.getListaPeliculas();
            for (Pelicula listaPeliculasPelicula : listaPeliculas) {
                listaPeliculasPelicula.setGenero(null);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
            }
            em.remove(genero);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Genero> findGeneroEntities() {
        return findGeneroEntities(true, -1, -1);
    }

    public List<Genero> findGeneroEntities(int maxResults, int firstResult) {
        return findGeneroEntities(false, maxResults, firstResult);
    }

    private List<Genero> findGeneroEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Genero.class));
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

    public Genero findGenero(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Genero.class, id);
        } finally {
            em.close();
        }
    }

    public int getGeneroCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Genero> rt = cq.from(Genero.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
