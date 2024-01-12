
package com.mycompany.filmoteca.persistencia;

import com.mycompany.filmoteca.logica.Anio;
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


public class AnioJpaController implements Serializable {

    public AnioJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public AnioJpaController() {
        emf = Persistence.createEntityManagerFactory("filmoteca");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Anio anio) throws PreexistingEntityException, Exception {
        if (anio.getListaPeliculas() == null) {
            anio.setListaPeliculas(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Pelicula> attachedListaPeliculas = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasPeliculaToAttach : anio.getListaPeliculas()) {
                listaPeliculasPeliculaToAttach = em.getReference(listaPeliculasPeliculaToAttach.getClass(), listaPeliculasPeliculaToAttach.getId());
                attachedListaPeliculas.add(listaPeliculasPeliculaToAttach);
            }
            anio.setListaPeliculas(attachedListaPeliculas);
            em.persist(anio);
            for (Pelicula listaPeliculasPelicula : anio.getListaPeliculas()) {
                Anio oldAnioOfListaPeliculasPelicula = listaPeliculasPelicula.getAnio();
                listaPeliculasPelicula.setAnio(anio);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
                if (oldAnioOfListaPeliculasPelicula != null) {
                    oldAnioOfListaPeliculasPelicula.getListaPeliculas().remove(listaPeliculasPelicula);
                    oldAnioOfListaPeliculasPelicula = em.merge(oldAnioOfListaPeliculasPelicula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findAnio(anio.getAnio()) != null) {
                throw new PreexistingEntityException("Anio " + anio + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Anio anio) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Anio persistentAnio = em.find(Anio.class, anio.getAnio());
            List<Pelicula> listaPeliculasOld = persistentAnio.getListaPeliculas();
            List<Pelicula> listaPeliculasNew = anio.getListaPeliculas();
            List<Pelicula> attachedListaPeliculasNew = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasNewPeliculaToAttach : listaPeliculasNew) {
                listaPeliculasNewPeliculaToAttach = em.getReference(listaPeliculasNewPeliculaToAttach.getClass(), listaPeliculasNewPeliculaToAttach.getId());
                attachedListaPeliculasNew.add(listaPeliculasNewPeliculaToAttach);
            }
            listaPeliculasNew = attachedListaPeliculasNew;
            anio.setListaPeliculas(listaPeliculasNew);
            anio = em.merge(anio);
            for (Pelicula listaPeliculasOldPelicula : listaPeliculasOld) {
                if (!listaPeliculasNew.contains(listaPeliculasOldPelicula)) {
                    listaPeliculasOldPelicula.setAnio(null);
                    listaPeliculasOldPelicula = em.merge(listaPeliculasOldPelicula);
                }
            }
            for (Pelicula listaPeliculasNewPelicula : listaPeliculasNew) {
                if (!listaPeliculasOld.contains(listaPeliculasNewPelicula)) {
                    Anio oldAnioOfListaPeliculasNewPelicula = listaPeliculasNewPelicula.getAnio();
                    listaPeliculasNewPelicula.setAnio(anio);
                    listaPeliculasNewPelicula = em.merge(listaPeliculasNewPelicula);
                    if (oldAnioOfListaPeliculasNewPelicula != null && !oldAnioOfListaPeliculasNewPelicula.equals(anio)) {
                        oldAnioOfListaPeliculasNewPelicula.getListaPeliculas().remove(listaPeliculasNewPelicula);
                        oldAnioOfListaPeliculasNewPelicula = em.merge(oldAnioOfListaPeliculasNewPelicula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                int id = anio.getAnio();
                if (findAnio(id) == null) {
                    throw new NonexistentEntityException("The anio with id " + id + " no longer exists.");
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
            Anio anio;
            try {
                anio = em.getReference(Anio.class, id);
                anio.getAnio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The anio with id " + id + " no longer exists.", enfe);
            }
            List<Pelicula> listaPeliculas = anio.getListaPeliculas();
            for (Pelicula listaPeliculasPelicula : listaPeliculas) {
                listaPeliculasPelicula.setAnio(null);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
            }
            em.remove(anio);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Anio> findAnioEntities() {
        return findAnioEntities(true, -1, -1);
    }

    public List<Anio> findAnioEntities(int maxResults, int firstResult) {
        return findAnioEntities(false, maxResults, firstResult);
    }

    private List<Anio> findAnioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Anio.class));
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

    public Anio findAnio(int id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Anio.class, id);
        } finally {
            em.close();
        }
    }

    public int getAnioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Anio> rt = cq.from(Anio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
