package com.kutuphane.hizmet;

import com.kutuphane.depo.RafDepo;
import com.kutuphane.varlik.Kitaplik;
import com.kutuphane.varlik.Raf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RafHizmet {
    
    private final RafDepo rafDepo;
    
    @Autowired
    public RafHizmet(RafDepo rafDepo) {
        this.rafDepo = rafDepo;
    }
    
    public List<Raf> tumRaflariGetir() {
        return rafDepo.findAll();
    }
    
    public Optional<Raf> rafGetir(Integer no) {
        return rafDepo.findById(no);
    }
    
    public List<Raf> rafAra(String isim) {
        return rafDepo.findByIsimContainingIgnoreCase(isim);
    }
    
    public List<Raf> kitaplikRaflariniGetir(Kitaplik kitaplik) {
        return rafDepo.findByKitaplik(kitaplik);
    }
    
    public Raf rafEkle(Raf raf) {
        return rafDepo.save(raf);
    }
    
    public Raf rafGuncelle(Integer no, Raf raf) {
        if (rafDepo.existsById(no)) {
            raf.setNo(no);
            return rafDepo.save(raf);
        }
        throw new IllegalArgumentException("Raf bulunamadı: " + no);
    }
    
    public void rafSil(Integer no) {
        rafDepo.deleteById(no);
    }
}