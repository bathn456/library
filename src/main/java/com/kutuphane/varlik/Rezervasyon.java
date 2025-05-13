package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rezervasyon")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rezervasyon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @ManyToOne
    @JoinColumn(name = "kullanici_no")
    private Kullanici kullanici;
    
    @Column(name = "rezervasyon_tarihi")
    private LocalDateTime rezervasyonTarihi;
    
    @Column(name = "durum")
    private String durum;
    
    @OneToMany(mappedBy = "rezervasyon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RezervasyonOgesi> rezervasyonOgeleri = new ArrayList<>();
    
    public void addRezervasyonOgesi(RezervasyonOgesi rezervasyonOgesi) {
        rezervasyonOgeleri.add(rezervasyonOgesi);
        rezervasyonOgesi.setRezervasyon(this);
    }
    
    public void removeRezervasyonOgesi(RezervasyonOgesi rezervasyonOgesi) {
        rezervasyonOgeleri.remove(rezervasyonOgesi);
        rezervasyonOgesi.setRezervasyon(null);
    }
}
