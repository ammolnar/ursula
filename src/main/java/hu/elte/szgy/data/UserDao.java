package hu.elte.szgy.data;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import hu.elte.szgy.data.Beteg;
@Transactional
@Repository
public class UserDao  {   // not using a dao interface here
    @PersistenceContext
    private EntityManager entityManager;

    public User findByUsername(String u) {
        return entityManager.find(User.class, u);
    }

	public void addUser(User u) {
		entityManager.persist(u);
	}
    
}
