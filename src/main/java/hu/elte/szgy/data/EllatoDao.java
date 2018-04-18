package hu.elte.szgy.data;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import hu.elte.szgy.data.Ellato;
@Transactional
@Repository
public class EllatoDao {
	@PersistenceContext	
	private EntityManager entityManager;	

    public Ellato getEllatoByTaj(int elid) {
		return entityManager.find(Ellato.class, elid);
	}

    public List<Ellato> getAllEllato() {
		String hql = "FROM Ellato as b ORDER BY b.szuldatum";
		return (List<Ellato>) entityManager.createQuery(hql).getResultList();
	}	
    public void addEllato(Ellato b) {
		entityManager.persist(b);
	}
    public void deleteEllato(int elid) {
		entityManager.remove(getEllatoByTaj(elid));
	}
} 
