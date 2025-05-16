package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kullanici")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kullanici {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "telefon_numarasi")
    private String telefonNumarasi;
    
    @Column(name = "ad")
    private String ad;
    
    @Column(name = "eposta")
    private String eposta;
    
    @Column(name = "uyelik_tarihi")
    private LocalDateTime uyelikTarihi;
    
    @Column(name = "durum")
    private String durum;
private Boolean ogrenciMi = false;
    
    @Column(name = "adres")
    private String adres;
    
    @Column(name = "soyad")
    private String soyad;
    
    @ManyToOne
    @JoinColumn(name = "rol_no")
    private KullaniciRol kullaniciRol;
    
    @OneToMany(mappedBy = "kullanici")
    private List<OduncAlma> oduncAlmalar = new ArrayList<>();
    
    @OneToMany(mappedBy = "kullanici")
    private List<Rezervasyon> rezervasyonlar = new ArrayList<>();
    
    @OneToMany(mappedBy = "kullanici")
    private List<Odeme> odemeler = new ArrayList<>();
}
