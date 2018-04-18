package hu.elte.szgy.data;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import hu.elte.szgy.data.Beteg;
@Transactional
@Repository
public class BetegDao implements BetegDaoIF {
	@PersistenceContext	
	private EntityManager entityManager;	

	@Override
    public Beteg getBetegByTaj(int taj) {
		return entityManager.find(Beteg.class, taj);
	}

	@Override
    public List<Beteg> getAllBeteg() {
		String hql = "FROM Beteg as b ORDER BY b.szuldatum";
		return (List<Beteg>) entityManager.createQuery(hql).getResultList();
	}	
	@Override
    public void addBeteg(Beteg b) {
		entityManager.persist(b);
	}
	@Override
    public void deleteBeteg(int taj) {
		entityManager.remove(getBetegByTaj(taj));
	}
} 
