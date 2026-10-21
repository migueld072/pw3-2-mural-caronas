package br.com.etechoracio.mural_caronas.repository;

import br.com.etechoracio.mural_caronas.entity.Carona;
import br.com.etechoracio.mural_caronas.entity.StatusCarona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaronaRepository extends JpaRepository<Carona, Long> {

    // Usado no filtro GET /api/caronas?status=DISPONIVEL (Aula 2)
    List<Carona> findByStatus(StatusCarona status);
}