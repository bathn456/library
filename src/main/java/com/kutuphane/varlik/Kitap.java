package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kitap")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kitap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "baslik")
    private String baslik;
    
    @Column(name = "yazar")
    private String yazar;
    
    @Column(name = "isbn")
    private String isbn;
    
    @Column(name = "yayin_yili")
    private LocalDateTime yayinYili;
    
    @Column(name = "toplam_adet")
    private Integer toplamAdet;
    
    @Column(name = "mevcut_adet")
    private Integer mevcutAdet;
    
    @ManyToOne
    @JoinColumn(name = "kategori_no")
    private Kategori kategori;

@ManyToOne
@JoinColumn(name = "raf_no")
private Raf raf;
    
    @ManyToOne
    @JoinColumn(name = "raf_no")
    private Raf raf;
    
    @OneToMany(mappedBy = "kitap")
    private List<OduncAlmaOgesi> oduncAlmaOgeleri = new ArrayList<>();
    
    @OneToMany(mappedBy = "kitap")
    private List<RezervasyonOgesi> rezervasyonOgeleri = new ArrayList<>();
    
    // Kitabın konumunu döndüren yardımcı metot
    public String getKonumBilgisi() {
        if (raf == null || raf.getKitaplik() == null || raf.getKitaplik().getOda() == null) {
            return "Konum bilgisi yok";
        }
        
        Oda oda = raf.getKitaplik().getOda();
        Kitaplik kitaplik = raf.getKitaplik();
        
        return String.format("Oda: %s, Kitaplık: %s, Raf: %s", 
                oda.getIsim(), kitaplik.getIsim(), raf.getIsim());
    }
}
