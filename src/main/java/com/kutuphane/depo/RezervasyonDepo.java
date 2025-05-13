package com.kutuphane.depo;

import com.kutuphane.varlik.Rezervasyon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RezervasyonDepo extends JpaRepository<Rezervasyon, Integer> {
    List<Rezervasyon> findByKullaniciNo(Integer kullaniciNo);
    
    List<Rezervasyon> findByDurum(String durum);
    
    @Query("SELECT r FROM Rezervasyon r WHERE r.rezervasyonTarihi < :date AND r.durum = 'BEKLIYOR'")
    List<Rezervasyon> findExpiredReservations(@Param("date") LocalDateTime date);
    
    @Query("SELECT r FROM Rezervasyon r JOIN r.rezervasyonOgeleri ro WHERE ro.kitap.no = :kitapNo")
    List<Rezervasyon> findByKitapNo(@Param("kitapNo") Integer kitapNo);
}
