package com.carro.service.Repository;

import com.carro.service.entitys.Carro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarroRepository extends JpaRepository<Carro,Integer> {

    List<Carro> findByUsuarioId(int usuarioId);
}
