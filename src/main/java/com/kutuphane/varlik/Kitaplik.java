package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kitaplik")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Kitaplik {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "isim")
    private String isim;
    
    @Column(name = "aciklama")
    private String aciklama;
    
    @ManyToOne
    @JoinColumn(name = "oda_no")
    private Oda oda;
    
    @OneToMany(mappedBy = "kitaplik", cascade = CascadeType.ALL)
    private List<Raf> raflar = new ArrayList<>();
}