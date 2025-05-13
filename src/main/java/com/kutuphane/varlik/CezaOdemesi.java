package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ceza_odemesi")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CezaOdemesi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "odenmis_tutar")
    private Integer odenmisTutar;
    
    @Column(name = "ceza_no")
    private Integer cezaNo;
    
    @ManyToOne
    @JoinColumn(name = "odeme_no")
    private Odeme odeme;
    
    @Column(name = "tarih_saat")
    private LocalDateTime tarihSaat;
    
    @Column(name = "odendi_mi")
    private Boolean odendiMi;
}
