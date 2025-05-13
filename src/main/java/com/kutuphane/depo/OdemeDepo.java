package com.kutuphane.depo;

import com.kutuphane.varlik.Odeme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OdemeDepo extends JpaRepository<Odeme, Integer> {
    List<Odeme> findByKullaniciNo(Integer kullaniciNo);
    
    List<Odeme> findByDurum(String durum);
    
    @Query("SELECT o FROM Odeme o WHERE o.odemeTarihi BETWEEN :startDate AND :endDate")
    List<Odeme> findPaymentsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT SUM(o.tutar) FROM Odeme o WHERE o.kullanici.no = :kullaniciNo AND o.durum = 'TAMAMLANDI'")
    Integer getTotalPaymentsByUser(@Param("kullaniciNo") Integer kullaniciNo);
}
