package br.com.etechoracio.mural_caronas.controller;

import br.com.etechoracio.mural_caronas.entity.Carona;
import br.com.etechoracio.mural_caronas.enums.StatusCarona;
import br.com.etechoracio.mural_caronas.service.CaronaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Libera o Angular (ng serve roda em http://localhost:4200) pra chamar a API
@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/caronas")
public class CaronaController {

    private final CaronaService service;

    public CaronaController(CaronaService service) {
        this.service = service;
    }

    // GET /api/caronas
    @GetMapping
    public List<Carona> listar(@RequestParam(required = false) StatusCarona status) {
        return service.listar(status);
    }

    // GET /api/caronas/{id}
    @GetMapping("/{id}")
    public Carona buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // POST /api/caronas
    @PostMapping
    public ResponseEntity<Carona> cadastrar(@RequestBody Carona carona) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.cadastrar(carona));
    }

    // PUT /api/caronas/{id}
    @PutMapping("/{id}")
    public Carona atualizar(@PathVariable Long id, @RequestBody Carona carona) {
        return service.atualizar(id, carona);
    }

    // DELETE /api/caronas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        service.excluir(id);
        return ResponseEntity.noContent().build();
    }

    // POST /api/caronas/{id}/reservar
    @PostMapping("/{id}/reservar")
    public Carona reservar(@PathVariable Long id) {
        return service.reservarVaga(id);
    }
}
