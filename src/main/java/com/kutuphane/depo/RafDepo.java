package com.kutuphane.depo;

import com.kutuphane.varlik.Kitaplik;
import com.kutuphane.varlik.Raf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RafDepo extends JpaRepository<Raf, Integer> {
    List<Raf> findByIsimContainingIgnoreCase(String isim);
    List<Raf> findByKitaplik(Kitaplik kitaplik);
}