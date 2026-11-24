package br.com.etechoracio.mural_caronas.service;

import br.com.etechoracio.mural_caronas.entity.Carona;
import br.com.etechoracio.mural_caronas.enums.StatusCarona;
import br.com.etechoracio.mural_caronas.repository.CaronaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CaronaService {

    private final CaronaRepository repository;

    public CaronaService(CaronaRepository repository) {
        this.repository = repository;
    }

    // GET /api/caronas
    public List<Carona> listar(StatusCarona status) {
        if (status != null) {
            return repository.findByStatusOrderByHorarioSaidaAsc(status);
        }
        return repository.findAllByOrderByHorarioSaidaAsc();
    }

    // GET /api/caronas/{id}
    public Carona buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada: " + id));
    }

    // POST /api/caronas
    public Carona cadastrar(Carona carona) {
        validar(carona);
        carona.setId(null);
        carona.setStatus(carona.getVagasDisponiveis() == 0 ? StatusCarona.LOTADO : StatusCarona.DISPONIVEL);
        return repository.save(carona);
    }

    // PUT /api/caronas/{id}
    public Carona atualizar(Long id, Carona dados) {
        validar(dados);
        Carona carona = buscarPorId(id);
        carona.setNomeMotoristaOuLider(dados.getNomeMotoristaOuLider());
        carona.setBairroOrigem(dados.getBairroOrigem());
        carona.setHorarioSaida(dados.getHorarioSaida());
        carona.setVagasDisponiveis(dados.getVagasDisponiveis());
        carona.setStatus(dados.getVagasDisponiveis() == 0 ? StatusCarona.LOTADO : StatusCarona.DISPONIVEL);
        return repository.save(carona);
    }

    // DELETE /api/caronas/{id}
    public void excluir(Long id) {
        Carona carona = buscarPorId(id);
        repository.delete(carona);
    }

    // POST /api/caronas/{id}/reservar  -> REGRA DE NEGÓCIO
    public Carona reservarVaga(Long id) {
        Carona carona = buscarPorId(id);

        if (carona.getVagasDisponiveis() <= 0 || carona.getStatus() == StatusCarona.LOTADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há vagas disponíveis para esta carona.");
        }

        carona.setVagasDisponiveis(carona.getVagasDisponiveis() - 1);

        if (carona.getVagasDisponiveis() == 0) {
            carona.setStatus(StatusCarona.LOTADO);
        }

        return repository.save(carona);
    }

    // Validações dos campos obrigatórios (devolve 400 com a mensagem)
    private void validar(Carona carona) {
        if (carona.getNomeMotoristaOuLider() == null || carona.getNomeMotoristaOuLider().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O nome do motorista ou líder é obrigatório.");
        }
        if (carona.getBairroOrigem() == null || carona.getBairroOrigem().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O bairro de origem é obrigatório.");
        }
        if (carona.getHorarioSaida() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O horário de saída é obrigatório.");
        }
        if (carona.getVagasDisponiveis() == null || carona.getVagasDisponiveis() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade de vagas não pode ser negativa.");
        }
    }
}
