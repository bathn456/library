package com.kutuphane.depo;

import com.kutuphane.varlik.Kullanici;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KullaniciDepo extends JpaRepository<Kullanici, Integer> {
    List<Kullanici> findByAdContainingIgnoreCaseOrSoyadContainingIgnoreCase(String ad, String soyad);
    
    @Query("SELECT k FROM Kullanici k WHERE k.kullaniciRol.rol = :rol")
    List<Kullanici> findByRol(@Param("rol") String rol);
    
    Kullanici findByEposta(String eposta);
}
