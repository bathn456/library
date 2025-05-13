package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "odeme")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Odeme {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @ManyToOne
    @JoinColumn(name = "kullanici_no")
    private Kullanici kullanici;
    
    @Column(name = "tutar")
    private Integer tutar;
    
    @Column(name = "odeme_tarihi")
    private LocalDateTime odemeTarihi;
    
    @Column(name = "odeme_yontemi")
    private String odemeYontemi;
    
    @Column(name = "durum")
    private String durum;
    
    @OneToMany(mappedBy = "odeme", cascade = CascadeType.ALL)
    private List<CezaOdemesi> cezaOdemeleri = new ArrayList<>();
}
