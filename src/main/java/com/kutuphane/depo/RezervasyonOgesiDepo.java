package com.kutuphane.depo;

import com.kutuphane.varlik.RezervasyonOgesi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RezervasyonOgesiDepo extends JpaRepository<RezervasyonOgesi, Integer> {
    List<RezervasyonOgesi> findByRezervasyonNo(Integer rezervasyonNo);
    
    List<RezervasyonOgesi> findByKitapNo(Integer kitapNo);
    
    List<RezervasyonOgesi> findByDurum(String durum);
}
