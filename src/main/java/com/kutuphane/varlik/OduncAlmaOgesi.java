package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "odunc_alma_ogesi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OduncAlmaOgesi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @ManyToOne
    @JoinColumn(name = "odunc_alma_no")
    private OduncAlma oduncAlma;
    
    @ManyToOne
    @JoinColumn(name = "kitap_no")
    private Kitap kitap;
    
    @Column(name = "adet")
    private Integer adet;
    
    @Column(name = "iade_tarihi")
    private LocalDateTime iadeTarihi;
    
    @Column(name = "durum")
    private String durum;
}
