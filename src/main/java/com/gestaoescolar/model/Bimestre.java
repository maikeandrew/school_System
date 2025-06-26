package com.gestaoescolar.model;

import java.util.ArrayList;
import java.util.List;

public class Bimestre {

    private final List<Double> notasTrabalhos;
    private double notaProva;

    public Bimestre() {
        this.notasTrabalhos = new ArrayList<>();
        this.notaProva = 0.0;
    }

    /**
     * Adiciona uma nota de trabalho ao bimestre.
     *
     * @param nota A nota do trabalho.
     */
    public void adicionarNotaTrabalho(double nota) {
        this.notasTrabalhos.add(nota);
    }

    /**
     * Define a nota da prova do bimestre.
     *
     * @param nota A nota da prova.
     */
    public void setNotaProva(double nota) {
        this.notaProva = nota;
    }

    /**
     * Calcula todas as notas dos trabalhos
     *
     * @return A soma dos trabalhos
     */
    public double calcularMediaTrabalhos() {
        if (notasTrabalhos.isEmpty()) {
            return 0.0;
        }
        double somaTrabalhos = 0.0;
        for (double nota : this.notasTrabalhos) {
            somaTrabalhos += nota;
        }
        return somaTrabalhos;
    }

    /**
     * Calcula a nota final do bimestre (trabalhos + prova)
     * avaliações (trabalhos e prova)
     *
     * @return A média do bimestre
     */
    public double calcularNotaBimestre() {
        double somaTotal = 0.0;
        for (double notaTrabalho : this.notasTrabalhos) {
            somaTotal += notaTrabalho;
        }
        return somaTotal += this.notaProva;

    }

    /**
     * Verifica se a média do bimestre e necessária para aprovação
     *
     * @param mediaBimestreParaAprovar A nota mínima para ser aprovado no bimestre.
     * @return true se a nota for suficiente, false caso contrário.
     */
    public boolean verificarAprovacaoBimestre(double mediaBimestreParaAprovar) {
        return calcularNotaBimestre() >= mediaBimestreParaAprovar;
    }

    //Getters
    public List<Double> getNotasTrabalhos() {
        return notasTrabalhos;
    }

    public double getNotaProva() {
        return notaProva;
    }
}
