package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "odunc_alma")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OduncAlma {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "odunc_alma_tarihi")
    private LocalDateTime oduncAlmaTarihi;
    
    @Column(name = "son_teslim_tarihi")
    private LocalDateTime sonTeslimTarihi;
    
    @Column(name = "durum")
    private String durum;
    
    @Column(name = "toplam_odunc")
    private Integer toplamOdunc;
    
    @ManyToOne
    @JoinColumn(name = "kullanici_no")
    private Kullanici kullanici;
    
    @OneToMany(mappedBy = "oduncAlma", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OduncAlmaOgesi> oduncAlmaOgeleri = new ArrayList<>();
    
    public void addOduncAlmaOgesi(OduncAlmaOgesi oduncAlmaOgesi) {
        oduncAlmaOgeleri.add(oduncAlmaOgesi);
        oduncAlmaOgesi.setOduncAlma(this);
    }
    
    public void removeOduncAlmaOgesi(OduncAlmaOgesi oduncAlmaOgesi) {
        oduncAlmaOgeleri.remove(oduncAlmaOgesi);
        oduncAlmaOgesi.setOduncAlma(null);
    }
}
