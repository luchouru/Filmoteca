
package com.mycompany.filmoteca.persistencia;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import com.mycompany.filmoteca.logica.Director;
import com.mycompany.filmoteca.logica.Pais;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.filmoteca.logica.Pelicula;
import com.mycompany.filmoteca.persistencia.exceptions.NonexistentEntityException;
import com.mycompany.filmoteca.persistencia.exceptions.PreexistingEntityException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class PaisJpaController implements Serializable {

    public PaisJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;
    
    public PaisJpaController() {
        emf = Persistence.createEntityManagerFactory("filmoteca");
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pais pais) throws PreexistingEntityException, Exception {
        if (pais.getListaDirectores() == null) {
            pais.setListaDirectores(new ArrayList<Director>());
        }
        if (pais.getListaPeliculas() == null) {
            pais.setListaPeliculas(new ArrayList<Pelicula>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Director> attachedListaDirectores = new ArrayList<Director>();
            for (Director listaDirectoresDirectorToAttach : pais.getListaDirectores()) {
                listaDirectoresDirectorToAttach = em.getReference(listaDirectoresDirectorToAttach.getClass(), listaDirectoresDirectorToAttach.getId());
                attachedListaDirectores.add(listaDirectoresDirectorToAttach);
            }
            pais.setListaDirectores(attachedListaDirectores);
            List<Pelicula> attachedListaPeliculas = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasPeliculaToAttach : pais.getListaPeliculas()) {
                listaPeliculasPeliculaToAttach = em.getReference(listaPeliculasPeliculaToAttach.getClass(), listaPeliculasPeliculaToAttach.getId());
                attachedListaPeliculas.add(listaPeliculasPeliculaToAttach);
            }
            pais.setListaPeliculas(attachedListaPeliculas);
            em.persist(pais);
            for (Director listaDirectoresDirector : pais.getListaDirectores()) {
                Pais oldNacionalidadOfListaDirectoresDirector = listaDirectoresDirector.getNacionalidad();
                listaDirectoresDirector.setNacionalidad(pais);
                listaDirectoresDirector = em.merge(listaDirectoresDirector);
                if (oldNacionalidadOfListaDirectoresDirector != null) {
                    oldNacionalidadOfListaDirectoresDirector.getListaDirectores().remove(listaDirectoresDirector);
                    oldNacionalidadOfListaDirectoresDirector = em.merge(oldNacionalidadOfListaDirectoresDirector);
                }
            }
            for (Pelicula listaPeliculasPelicula : pais.getListaPeliculas()) {
                Pais oldPaisOfListaPeliculasPelicula = listaPeliculasPelicula.getPais();
                listaPeliculasPelicula.setPais(pais);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
                if (oldPaisOfListaPeliculasPelicula != null) {
                    oldPaisOfListaPeliculasPelicula.getListaPeliculas().remove(listaPeliculasPelicula);
                    oldPaisOfListaPeliculasPelicula = em.merge(oldPaisOfListaPeliculasPelicula);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPais(pais.getNombre()) != null) {
                throw new PreexistingEntityException("Pais " + pais + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pais pais) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pais persistentPais = em.find(Pais.class, pais.getNombre());
            List<Director> listaDirectoresOld = persistentPais.getListaDirectores();
            List<Director> listaDirectoresNew = pais.getListaDirectores();
            List<Pelicula> listaPeliculasOld = persistentPais.getListaPeliculas();
            List<Pelicula> listaPeliculasNew = pais.getListaPeliculas();
            List<Director> attachedListaDirectoresNew = new ArrayList<Director>();
            for (Director listaDirectoresNewDirectorToAttach : listaDirectoresNew) {
                listaDirectoresNewDirectorToAttach = em.getReference(listaDirectoresNewDirectorToAttach.getClass(), listaDirectoresNewDirectorToAttach.getId());
                attachedListaDirectoresNew.add(listaDirectoresNewDirectorToAttach);
            }
            listaDirectoresNew = attachedListaDirectoresNew;
            pais.setListaDirectores(listaDirectoresNew);
            List<Pelicula> attachedListaPeliculasNew = new ArrayList<Pelicula>();
            for (Pelicula listaPeliculasNewPeliculaToAttach : listaPeliculasNew) {
                listaPeliculasNewPeliculaToAttach = em.getReference(listaPeliculasNewPeliculaToAttach.getClass(), listaPeliculasNewPeliculaToAttach.getId());
                attachedListaPeliculasNew.add(listaPeliculasNewPeliculaToAttach);
            }
            listaPeliculasNew = attachedListaPeliculasNew;
            pais.setListaPeliculas(listaPeliculasNew);
            pais = em.merge(pais);
            for (Director listaDirectoresOldDirector : listaDirectoresOld) {
                if (!listaDirectoresNew.contains(listaDirectoresOldDirector)) {
                    listaDirectoresOldDirector.setNacionalidad(null);
                    listaDirectoresOldDirector = em.merge(listaDirectoresOldDirector);
                }
            }
            for (Director listaDirectoresNewDirector : listaDirectoresNew) {
                if (!listaDirectoresOld.contains(listaDirectoresNewDirector)) {
                    Pais oldNacionalidadOfListaDirectoresNewDirector = listaDirectoresNewDirector.getNacionalidad();
                    listaDirectoresNewDirector.setNacionalidad(pais);
                    listaDirectoresNewDirector = em.merge(listaDirectoresNewDirector);
                    if (oldNacionalidadOfListaDirectoresNewDirector != null && !oldNacionalidadOfListaDirectoresNewDirector.equals(pais)) {
                        oldNacionalidadOfListaDirectoresNewDirector.getListaDirectores().remove(listaDirectoresNewDirector);
                        oldNacionalidadOfListaDirectoresNewDirector = em.merge(oldNacionalidadOfListaDirectoresNewDirector);
                    }
                }
            }
            for (Pelicula listaPeliculasOldPelicula : listaPeliculasOld) {
                if (!listaPeliculasNew.contains(listaPeliculasOldPelicula)) {
                    listaPeliculasOldPelicula.setPais(null);
                    listaPeliculasOldPelicula = em.merge(listaPeliculasOldPelicula);
                }
            }
            for (Pelicula listaPeliculasNewPelicula : listaPeliculasNew) {
                if (!listaPeliculasOld.contains(listaPeliculasNewPelicula)) {
                    Pais oldPaisOfListaPeliculasNewPelicula = listaPeliculasNewPelicula.getPais();
                    listaPeliculasNewPelicula.setPais(pais);
                    listaPeliculasNewPelicula = em.merge(listaPeliculasNewPelicula);
                    if (oldPaisOfListaPeliculasNewPelicula != null && !oldPaisOfListaPeliculasNewPelicula.equals(pais)) {
                        oldPaisOfListaPeliculasNewPelicula.getListaPeliculas().remove(listaPeliculasNewPelicula);
                        oldPaisOfListaPeliculasNewPelicula = em.merge(oldPaisOfListaPeliculasNewPelicula);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = pais.getNombre();
                if (findPais(id) == null) {
                    throw new NonexistentEntityException("The pais with id " + id + " no longer exists.");
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
            Pais pais;
            try {
                pais = em.getReference(Pais.class, id);
                pais.getNombre();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pais with id " + id + " no longer exists.", enfe);
            }
            List<Director> listaDirectores = pais.getListaDirectores();
            for (Director listaDirectoresDirector : listaDirectores) {
                listaDirectoresDirector.setNacionalidad(null);
                listaDirectoresDirector = em.merge(listaDirectoresDirector);
            }
            List<Pelicula> listaPeliculas = pais.getListaPeliculas();
            for (Pelicula listaPeliculasPelicula : listaPeliculas) {
                listaPeliculasPelicula.setPais(null);
                listaPeliculasPelicula = em.merge(listaPeliculasPelicula);
            }
            em.remove(pais);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pais> findPaisEntities() {
        return findPaisEntities(true, -1, -1);
    }

    public List<Pais> findPaisEntities(int maxResults, int firstResult) {
        return findPaisEntities(false, maxResults, firstResult);
    }

    private List<Pais> findPaisEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pais.class));
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

    public Pais findPais(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pais.class, id);
        } finally {
            em.close();
        }
    }

    public int getPaisCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pais> rt = cq.from(Pais.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
