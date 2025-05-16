package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "raf")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Raf {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "isim")
    private String isim;
    
    @Column(name = "aciklama")
    private String aciklama;
    
    @Column(name = "kapasite")
    private Integer kapasite;
    
    @ManyToOne
    @JoinColumn(name = "kitaplik_no")
    private Kitaplik kitaplik;
    
    @OneToMany(mappedBy = "raf")
    private List<Kitap> kitaplar = new ArrayList<>();
}