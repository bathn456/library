package com.kutuphane.depo;

import com.kutuphane.varlik.Kategori;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KategoriDepo extends JpaRepository<Kategori, Integer> {
    List<Kategori> findByIsimContainingIgnoreCase(String isim);
    
    List<Kategori> findByUstKategoriNo(Integer ustKategoriNo);
    
    List<Kategori> findByUstKategoriNoIsNull();
}
