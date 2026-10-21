package br.com.etechoracio.mural_caronas.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Entity
@Table(name = "TBL_CARONA")
@Getter
@Setter
@NoArgsConstructor
public class Carona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CARONA")
    private Long id;

    @Column(name = "TX_NOME_MOTORISTA_LIDER", nullable = false, length = 100)
    private String nomeMotoristaOuLider;

    @Column(name = "TX_BAIRRO_ORIGEM", nullable = false, length = 100)
    private String bairroOrigem;

    @Column(name = "HR_SAIDA", nullable = false)
    private LocalTime horarioSaida;

    @Column(name = "NR_VAGAS_DISPONIVEIS", nullable = false)
    private Integer vagasDisponiveis;

    @Enumerated(EnumType.STRING)
    @Column(name = "TX_STATUS", nullable = false, length = 20)
    private StatusCarona status = StatusCarona.DISPONIVEL;
}