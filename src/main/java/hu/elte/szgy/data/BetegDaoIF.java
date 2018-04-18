package hu.elte.szgy.data;
import java.util.List;
//import hu.elte.szgy.data.Beteg;
public interface BetegDaoIF {
    public List<Beteg> getAllBeteg();
    public Beteg getBetegByTaj(int taj);
    public void addBeteg(Beteg b);
    public void deleteBeteg(int taj);
} 
