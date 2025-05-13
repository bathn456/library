package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rezervasyon_ogesi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RezervasyonOgesi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @ManyToOne
    @JoinColumn(name = "rezervasyon_no")
    private Rezervasyon rezervasyon;
    
    @ManyToOne
    @JoinColumn(name = "kitap_no")
    private Kitap kitap;
    
    @Column(name = "adet")
    private Integer adet;
    
    @Column(name = "durum")
    private String durum;
}
