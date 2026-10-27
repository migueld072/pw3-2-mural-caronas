package br.com.etechoracio.mural_caronas.service;

import br.com.etechoracio.mural_caronas.entity.Carona;
import br.com.etechoracio.mural_caronas.entity.StatusCarona;
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

    // GET /caronas
    public List<Carona> listar(StatusCarona status) {
        if (status != null) {
            return repository.findByStatus(status);
        }
        return repository.findAll();
    }

    // GET /caronas/{id}
    public Carona buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada: " + id));
    }

    // POST /caronas
    public Carona cadastrar(Carona carona) {
        carona.setId(null);
        carona.setStatus(carona.getVagasDisponiveis() == 0 ? StatusCarona.LOTADO : StatusCarona.DISPONIVEL);
        return repository.save(carona);
    }

    // PUT /caronas/{id}
    public Carona atualizar(Long id, Carona dados) {
        Carona carona = buscarPorId(id);
        carona.setNomeMotoristaOuLider(dados.getNomeMotoristaOuLider());
        carona.setBairroOrigem(dados.getBairroOrigem());
        carona.setHorarioSaida(dados.getHorarioSaida());
        carona.setVagasDisponiveis(dados.getVagasDisponiveis());
        carona.setStatus(dados.getVagasDisponiveis() == 0 ? StatusCarona.LOTADO : StatusCarona.DISPONIVEL);
        return repository.save(carona);
    }

    // DELETE /caronas/{id}
    public void excluir(Long id) {
        Carona carona = buscarPorId(id);
        repository.delete(carona);
    }

    // POST /caronas/{id}/reservar  -> REGRA DE NEGÓCIO
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
}
