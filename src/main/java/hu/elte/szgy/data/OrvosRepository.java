package hu.elte.szgy.data;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import hu.elte.szgy.data.Ellato;
@Transactional
@Repository
public interface OrvosRepository extends JpaRepository<Orvos,Integer> {
    public Orvos findOrvosByNev(String name);
	List<Orvos> findOrvosByOsztaly(Osztaly osztaly);
} 
