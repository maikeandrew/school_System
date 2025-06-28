package com.gestaoescolar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DisciplinaTest {

    private Disciplina disciplina;

    @BeforeEach
    void setUp(){
        disciplina = new Disciplina("Matematica");
    }

    @Test
    @DisplayName("Deve calcular corretamente a media final")
    void deveCalcularCorretamenteMediaFinal(){

        disciplina.getBimestre(1).setNotaProva(8.0);
        disciplina.getBimestre(2).setNotaProva(7.0);
        disciplina.getBimestre(3).setNotaProva(6.0);
        disciplina.getBimestre(4).setNotaProva(10.0);

        double mediaAnualEsperado = 7.75;

        double mediaAnualCalculada = disciplina.calcularMediaFinalAnual();

        assertEquals(mediaAnualEsperado, mediaAnualCalculada, 0.001);
    }

    @Test
    @DisplayName("Verifica corretamente se foi aprovado")
    void deveVerificarAprovacaoFinallCorretamente(){

        disciplina.getBimestre(1).setNotaProva(7.0);
        disciplina.getBimestre(2).setNotaProva(7.0);
        disciplina.getBimestre(3).setNotaProva(7.0);
        disciplina.getBimestre(4).setNotaProva(7.0);

        double mediaParaAprovacao = 7.0;

        boolean foiAprovado = disciplina.verificarAprovacaoFinal(mediaParaAprovacao);

        assertTrue(foiAprovado);
    }

    @Test
    @DisplayName("Verifica corretamente se foi reprovado")
    void deveVerificarReprovacaoFinalCorretamente(){

        disciplina.getBimestre(1).setNotaProva(6.0);
        disciplina.getBimestre(2).setNotaProva(6.0);
        disciplina.getBimestre(3).setNotaProva(6.0);
        disciplina.getBimestre(4).setNotaProva(6.0);

        double mediaParaAprovacao = 7.0;

        boolean foiReprovado = disciplina.verificarAprovacaoFinal(mediaParaAprovacao);

        assertFalse(foiReprovado);
    }
}