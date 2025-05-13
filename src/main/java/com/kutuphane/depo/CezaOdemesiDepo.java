package com.kutuphane.depo;

import com.kutuphane.varlik.CezaOdemesi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CezaOdemesiDepo extends JpaRepository<CezaOdemesi, Integer> {
    List<CezaOdemesi> findByOdemeNo(Integer odemeNo);
    
    List<CezaOdemesi> findByCezaNo(Integer cezaNo);
    
    List<CezaOdemesi> findByOdendiMi(Boolean odendiMi);
    
    @Query("SELECT SUM(co.odenmisTutar) FROM CezaOdemesi co WHERE co.odeme.kullanici.no = :kullaniciNo AND co.odendiMi = true")
    Integer getTotalPenaltiesPaidByUser(@Param("kullaniciNo") Integer kullaniciNo);
}
