package com.kutuphane.hizmet;

import com.kutuphane.depo.KitaplikDepo;
import com.kutuphane.varlik.Kitaplik;
import com.kutuphane.varlik.Oda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitaplikHizmet {
    
    private final KitaplikDepo kitaplikDepo;
    
    @Autowired
    public KitaplikHizmet(KitaplikDepo kitaplikDepo) {
        this.kitaplikDepo = kitaplikDepo;
    }
    
    public List<Kitaplik> tumKitapliklariGetir() {
        return kitaplikDepo.findAll();
    }
    
    public Optional<Kitaplik> kitaplikGetir(Integer no) {
        return kitaplikDepo.findById(no);
    }
    
    public List<Kitaplik> kitaplikAra(String isim) {
        return kitaplikDepo.findByIsimContainingIgnoreCase(isim);
    }
    
    public List<Kitaplik> odaKitapliklariniGetir(Oda oda) {
        return kitaplikDepo.findByOda(oda);
    }
    
    public Kitaplik kitaplikEkle(Kitaplik kitaplik) {
        return kitaplikDepo.save(kitaplik);
    }
    
    public Kitaplik kitaplikGuncelle(Integer no, Kitaplik kitaplik) {
        if (kitaplikDepo.existsById(no)) {
            kitaplik.setNo(no);
            return kitaplikDepo.save(kitaplik);
        }
        throw new IllegalArgumentException("Kitaplık bulunamadı: " + no);
    }
    
    public void kitaplikSil(Integer no) {
        kitaplikDepo.deleteById(no);
    }
}