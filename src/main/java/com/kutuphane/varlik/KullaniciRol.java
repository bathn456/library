package com.kutuphane.varlik;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "kullanici_rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KullaniciRol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer no;
    
    @Column(name = "rol")
    private String rol = "YONETICI"; // Sadece yönetici rolü
    
    @OneToMany(mappedBy = "kullaniciRol")
    private List<Kullanici> kullanicilar = new ArrayList<>();
}
