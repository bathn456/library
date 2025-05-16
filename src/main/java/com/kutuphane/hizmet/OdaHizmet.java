package com.kutuphane.hizmet;

import com.kutuphane.depo.OdaDepo;
import com.kutuphane.varlik.Oda;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OdaHizmet {
    
    private final OdaDepo odaDepo;
    
    @Autowired
    public OdaHizmet(OdaDepo odaDepo) {
        this.odaDepo = odaDepo;
    }
    
    public List<Oda> tumOdalariGetir() {
        return odaDepo.findAll();
    }
    
    public Optional<Oda> odaGetir(Integer no) {
        return odaDepo.findById(no);
    }
    
    public List<Oda> odaAra(String isim) {
        return odaDepo.findByIsimContainingIgnoreCase(isim);
    }
    
    public Oda odaEkle(Oda oda) {
        return odaDepo.save(oda);
    }
    
    public Oda odaGuncelle(Integer no, Oda oda) {
        if (odaDepo.existsById(no)) {
            oda.setNo(no);
            return odaDepo.save(oda);
        }
        throw new IllegalArgumentException("Oda bulunamadı: " + no);
    }
    
    public void odaSil(Integer no) {
        odaDepo.deleteById(no);
    }
}