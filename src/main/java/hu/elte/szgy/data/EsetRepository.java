package hu.elte.szgy.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import hu.elte.szgy.data.Beteg;
public interface EsetRepository extends JpaRepository<Eset, Integer> {
	List<Eset> findEsetekByBeteg(Beteg beteg);
} 
