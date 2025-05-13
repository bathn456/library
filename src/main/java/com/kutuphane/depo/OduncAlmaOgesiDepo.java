package com.kutuphane.depo;

import com.kutuphane.varlik.OduncAlmaOgesi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OduncAlmaOgesiDepo extends JpaRepository<OduncAlmaOgesi, Integer> {
    List<OduncAlmaOgesi> findByOduncAlmaNo(Integer oduncAlmaNo);
    
    List<OduncAlmaOgesi> findByKitapNo(Integer kitapNo);
    
    @Query("SELECT oao FROM OduncAlmaOgesi oao WHERE oao.durum = 'TESLIM_EDILMEDI' AND oao.oduncAlma.kullanici.no = :kullaniciNo")
    List<OduncAlmaOgesi> findActiveItemsByUserId(@Param("kullaniciNo") Integer kullaniciNo);
}
