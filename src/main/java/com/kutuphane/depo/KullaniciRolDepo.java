package com.kutuphane.depo;

import com.kutuphane.varlik.KullaniciRol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KullaniciRolDepo extends JpaRepository<KullaniciRol, Integer> {
    KullaniciRol findByRol(String rol);
}
