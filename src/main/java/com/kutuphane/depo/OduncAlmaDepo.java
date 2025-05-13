package com.kutuphane.depo;

import com.kutuphane.varlik.OduncAlma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OduncAlmaDepo extends JpaRepository<OduncAlma, Integer> {
    List<OduncAlma> findByKullaniciNo(Integer kullaniciNo);
    
    List<OduncAlma> findByDurum(String durum);
    
    @Query("SELECT o FROM OduncAlma o WHERE o.sonTeslimTarihi < :now AND o.durum = 'AKTIF'")
    List<OduncAlma> findOverdueLoans(@Param("now") LocalDateTime now);
    
    @Query("SELECT o FROM OduncAlma o JOIN o.oduncAlmaOgeleri oao WHERE oao.kitap.no = :kitapNo")
    List<OduncAlma> findByKitapNo(@Param("kitapNo") Integer kitapNo);
}
