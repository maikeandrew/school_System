import java.util.ArrayList;
import java.util.List;

public class Turma {
    private String nomeDaTurma;
    private List<Aluno> alunos;

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

    /*
    public void listarAlunos(){
        System.out.println("--- Alunos da turma: " + nomeDaTurma + " ---");
        for (Aluno aluno : alunos){
            System.out.printf("Nome: %-20s | Notas: %.1f  | Media: %.1f | Sistuacao: %s\n",
                    aluno.getNome(),
                    aluno.calcMedia(),
                    aluno.verficarAprovacao(7.0) ? "Aprovado" : "Reprovado"
            );
        }
    }
    */

    public void listarAlunos(){
        System.out.println("--- Relatorio da turma: " + nomeDaTurma + " ---");
        for (Aluno aluno : alunos){
            System.out.printf("Nome: %-20s | Notas: ", aluno.getNome());
            List<Double> notas = aluno.getNotas();

            if (notas.isEmpty()){
                System.out.print("[Nemhuma nota lancada]");
            } else {
                for (double nota : notas){
                    System.out.printf("%.1f ", nota);
                }
            }
            System.out.printf("| Media: %.1f | Sistuacao: %s\n",
                    aluno.calcMedia(),
                    aluno.verficarAprovacao(7.0) ? "Aprovado" : "Reprovado"
            );
        }
        System.out.println("--- Fim do Relatório ---");
    }

    public List<Aluno> getAlunos(){
        return alunos;
    }
}
