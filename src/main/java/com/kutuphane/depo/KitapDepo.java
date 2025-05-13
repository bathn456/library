package com.kutuphane.depo;

import com.kutuphane.varlik.Kitap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KitapDepo extends JpaRepository<Kitap, Integer> {
    List<Kitap> findByBaslikContainingIgnoreCase(String baslik);
    List<Kitap> findByYazarContainingIgnoreCase(String yazar);
    List<Kitap> findByKategoriNo(Integer kategoriNo);
    
    @Query("SELECT k FROM Kitap k WHERE k.mevcutAdet > 0")
    List<Kitap> findAllAvailableBooks();
    
    @Query("SELECT k FROM Kitap k WHERE k.mevcutAdet = 0")
    List<Kitap> findAllUnavailableBooks();
}
