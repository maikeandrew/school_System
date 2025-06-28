package com.gestaoescolar.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Teste de funcionalida - Turma")
class TurmaTest {

    private Turma turma;

    @BeforeEach
    void setUp() {
        turma = new Turma("TesteTurma");
    }

    @Test
    @DisplayName("Deve add um aluno a turma")
    void deveAdicionarUmAlunoCorretamente() {

        Aluno novoAluno = new Aluno("Aluno1");

        turma.addAluno(novoAluno);

        assertEquals(1, turma.getAlunos().size());
        assertEquals("Aluno1", turma.buscarAluno("Aluno1").getNome());
    }

    @Test
    @DisplayName("Deve matricular um aluno a disciplina existente")
    void deveMatricularAlunoEmDisciplinaExistente() {

        turma.addDisciplinaAoCurriculo("Matematica");
        turma.addDisciplinaAoCurriculo("Portugues");

        Aluno aluno1 = new Aluno("Aluno1");

        turma.addAluno(aluno1);

        Aluno alunoAdicionado = turma.buscarAluno("Aluno1");

        assertNotNull(alunoAdicionado);
        assertEquals(2, alunoAdicionado.getDisciplinas().size());
        assertEquals("Matematica", alunoAdicionado.getDisciplinas().get(0).getNome());
    }

    @Test
    @DisplayName("Deve add uma nova disciplina")
    void deveAdicionaUmNovaDisciplinaCorretamente() {

        turma.addAluno(new Aluno("Aluno1"));
        turma.addAluno(new Aluno("Aluno2"));

        turma.addDisciplinaAoCurriculo("Ingles");

        Aluno aluno1 = turma.buscarAluno("Aluno1");
        Aluno aluno2 = turma.buscarAluno("Aluno2");

        assertEquals(1, aluno1.getDisciplinas().size());
        assertEquals(1, aluno2.getDisciplinas().size());
        assertEquals("Ingles", aluno1.getDisciplinas().get(0).getNome());
    }

    @Test
    @DisplayName("Deve remover um aluno da turma")
    void deveRemoverAlunoCorretamente() {

        Aluno alunoAtual = new Aluno("Aluno1");
        turma.addAluno(alunoAtual);
        assertEquals(1, turma.getAlunos().size());

        boolean foiRemovido = turma.removerAluno("Aluno1");


        assertTrue(foiRemovido);
        assertEquals(0, turma.getAlunos().size());
        assertNull(turma.buscarAluno("Aluno1"));
    }
}