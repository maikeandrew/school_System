import java.util.ArrayList;
import java.util.List;

public class Disciplina {

    private String nome;
    private List<Double> notas;

    public Disciplina(String nome) {
        this.nome = nome;
        this.notas = new ArrayList<>();
    }

    // Adiciona uma nota a discipina separadamente
    public boolean addNota(double nota) {
        if (nota >= 0 && nota <= 10) {
            this.notas.add(nota);
            return true;
        } else {
            return false;
        }
    }

    // Alterar as notas selecionadas dos alunos
    public boolean alterarNota(int indiceNota, double novaNota) {
        //Valida de o indice esta dentro dos limites 0 a 10
        if (indiceNota >= 0 && indiceNota < this.notas.size() && novaNota >= 0 && novaNota <= 10) {
            this.notas.set(indiceNota, novaNota);
            return true;
        }
        return false;
    }

    // Remove uma nota selecionada
    public boolean removerNota(int indiceNota) {
        // Validacao do indice
        if (indiceNota >= 0 && indiceNota < this.notas.size()) {
            this.notas.remove(indiceNota);
            return true;
        }
        return false;
    }


    // Calcaula a media das notas para cada disciplina
    public double calcMedia() {
        if (notas.isEmpty()) {
            return 0.0;
        }
        double soma = 0.0;
        for (double nota : this.notas) {
            soma += nota;
        }
        return soma / 4; //Calculo da media
    }

    // verificar se o aluno estar aprovado na materia
    public boolean verificarAprovacao(double mediaParaAprovar) {
        return calcMedia() >= mediaParaAprovar;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public List<Double> getNotas() {
        return notas;
    }
}
