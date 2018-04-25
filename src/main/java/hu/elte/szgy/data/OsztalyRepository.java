package hu.elte.szgy.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import hu.elte.szgy.data.Beteg;
@Transactional
@Repository
public interface OsztalyRepository extends JpaRepository<Osztaly,String>  {  
}
