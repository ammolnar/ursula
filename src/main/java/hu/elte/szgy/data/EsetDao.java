package hu.elte.szgy.data;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
//import hu.elte.szgy.data.Eset;
@Transactional
@Repository
public class EsetDao {
	@PersistenceContext	
	private EntityManager entityManager;	

    public Eset getEsetById(int esid) {
		return entityManager.find(Eset.class, esid);
	}

    public Set<Eset> getKezelesForBeteg(Beteg b) {
		Beteg bb = entityManager.merge(b);
        return bb.getEsetek();
    }

    public void addEset(Eset e) {
		entityManager.persist(e);
	}
    public void deleteEset(int esid) {
		entityManager.remove(getEsetById(esid));
	}
} 
