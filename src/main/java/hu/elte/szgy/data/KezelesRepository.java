package hu.elte.szgy.data;

import org.springframework.data.jpa.repository.JpaRepository;
//import hu.elte.szgy.data.Beteg;
public interface KezelesRepository extends JpaRepository<Beteg, Integer> {
} 
