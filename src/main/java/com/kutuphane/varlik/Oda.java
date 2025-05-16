package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "oda")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Oda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "isim")
    private String isim;
    
    @Column(name = "aciklama")
    private String aciklama;
    
    @Column(name = "kat")
    private Integer kat;
    
    @OneToMany(mappedBy = "oda", cascade = CascadeType.ALL)
    private List<Kitaplik> kitapliklar = new ArrayList<>();
}