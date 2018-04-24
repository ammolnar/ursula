package hu.elte.szgy.data;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import hu.elte.szgy.data.Ellato;
@Transactional
@Repository
public interface EllatoRepository extends JpaRepository<Ellato,Integer> {
    public List<Ellato> findAll();
    public Ellato findByNev(String name);
} 
