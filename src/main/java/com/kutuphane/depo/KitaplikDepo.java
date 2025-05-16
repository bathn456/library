package com.kutuphane.depo;

import com.kutuphane.varlik.Kitaplik;
import com.kutuphane.varlik.Oda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitaplikDepo extends JpaRepository<Kitaplik, Integer> {
    List<Kitaplik> findByIsimContainingIgnoreCase(String isim);
    List<Kitaplik> findByOda(Oda oda);
}