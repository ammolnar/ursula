package hu.elte.szgy.data;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

//import hu.elte.szgy.data.Kezeles;
@Transactional
@Repository
public class KezelesDao {
	@PersistenceContext	
	private EntityManager entityManager;	

    public Kezeles getKezelesById(int kid) {
		return entityManager.find(Kezeles.class, kid);
	}

    public Set<Kezeles> getKezelesForEset(Eset e) {
		Eset ee = entityManager.find(Eset.class, e.getEsid());
        return ee.getKezelesek();
	}	
    public void addKezeles(Kezeles k) {
		entityManager.persist(k);
	}
    public void deleteKezeles(int kid) {
		entityManager.remove(getKezelesById(kid));
	}
} 
