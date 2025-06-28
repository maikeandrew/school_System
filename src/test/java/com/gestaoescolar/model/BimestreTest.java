package com.gestaoescolar.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BimestreTest {

    @Test
    @DisplayName("Deve calcular a nota de cada bimestre corretamente")
    void deveCalcularNotaBimestralCorretamenteComTrabalhosEProva(){

        Bimestre bimestre = new Bimestre();
        bimestre.adicionarNotaTrabalho(1.0);
        bimestre.adicionarNotaTrabalho(2.0);
        bimestre.setNotaProva(7.0);

        double resultadoEsperado = 10.0;

        double resultadoCalculado = bimestre.calcularNotaBimestre();

        assertEquals(resultadoEsperado, resultadoCalculado, 0.001);

    }

    @Test
    @DisplayName("Deve calcular corretamente so das provas")
    void deveCalcularNotaCorretamenteApenasComProva(){

        Bimestre bimestre = new Bimestre();
        bimestre.adicionarNotaTrabalho(0.0);
        bimestre.adicionarNotaTrabalho(0.0);
        bimestre.setNotaProva(6.0);

        double resultadoEsperado = 6.0;

        double rescultadoCalculado = bimestre.calcularNotaBimestre();

        assertEquals(resultadoEsperado, rescultadoCalculado, 0.001);
    }

}