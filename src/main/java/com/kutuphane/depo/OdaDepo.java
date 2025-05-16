package com.kutuphane.depo;

import com.kutuphane.varlik.Oda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OdaDepo extends JpaRepository<Oda, Integer> {
    List<Oda> findByIsimContainingIgnoreCase(String isim);
}