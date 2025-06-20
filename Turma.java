import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String nomeDaTurma;
    private List<Aluno> alunos;
    private static final double MEDIA_PARA_APROVACAO = 7.0;

    public Turma(String nomeDaTurma) {
        this.nomeDaTurma = nomeDaTurma;
        this.alunos = new ArrayList<>();
    }

    public void addAluno(Aluno aluno){
        this.alunos.add(aluno);
    }

    public Aluno buscarAluno(String nome){
        for(Aluno aluno : alunos){
            if(aluno.getNome().equalsIgnoreCase(nome)){
                return aluno;
            }
        }
        return null; //Caso nao encontre
    }

    public List<Aluno> getAlunos(){
        return alunos;
    }

    public String getNomeDaTurma(){
        return this.nomeDaTurma;
    }
}
