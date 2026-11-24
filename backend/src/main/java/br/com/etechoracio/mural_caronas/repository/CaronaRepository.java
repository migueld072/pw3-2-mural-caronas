package br.com.etechoracio.mural_caronas.repository;

import br.com.etechoracio.mural_caronas.entity.Carona;
import br.com.etechoracio.mural_caronas.enums.StatusCarona;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CaronaRepository extends JpaRepository<Carona, Long> {

    // Lista todas, ordenadas pelo horário de saída
    List<Carona> findAllByOrderByHorarioSaidaAsc();

    // Filtro GET /api/caronas?status=DISPONIVEL, também ordenado pelo horário
    List<Carona> findByStatusOrderByHorarioSaidaAsc(StatusCarona status);
}
