package hu.elte.szgy.data;

import org.springframework.data.jpa.repository.JpaRepository;
//import hu.elte.szgy.data.Beteg;
public interface BetegRepository extends JpaRepository<Beteg, Integer> {
	int hasPendingKezelesBy(int taj, int elid);
} 
