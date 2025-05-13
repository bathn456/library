package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kategori")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kategori {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "isim")
    private String isim;
    
    @Column(name = "aciklama")
    private String aciklama;
    
    @Column(name = "ust_kategori_no")
    private Integer ustKategoriNo;
    
    @OneToMany(mappedBy = "kategori")
    private List<Kitap> kitaplar = new ArrayList<>();
}
