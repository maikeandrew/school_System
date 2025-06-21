import java.util.ArrayList;
import java.util.List;

public class Aluno {
    // Encapsulamento
    private String nome;
    private List<Disciplina> disciplinas;

    public Aluno(String nome) {
        this.nome = nome;
        this.disciplinas = new ArrayList<>();
    }

    // Adicionar diciplina para o aluno
    public void addDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
    }

    //Buscar a disciplina pelo nome
    public Disciplina buscarDisciplina(String nome) {
        for (Disciplina disciplina : this.disciplinas) {
            if (disciplina.getNome().equalsIgnoreCase(nome)) {
                return disciplina;
            }
        }
        return null;
    }


    //getters
    public String getNome() {
        return this.nome;
    }

    public List<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public void setNome(String novoNome) {
        this.nome = novoNome;
    }

}