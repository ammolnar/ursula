package hu.elte.szgy.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import hu.elte.szgy.data.Beteg;
public interface KezelesRepository extends JpaRepository<Kezeles, Integer> {
	List<Kezeles> findByEset(Eset eset);
} 
