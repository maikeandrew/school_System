public class Escola {

    public static void main(String[] args){
        final double MEDIA_MINIMA = 7.0;

        System.out.println("-> Cadasto do Aluno <-");
        Aluno aluno1 = new Aluno("Deivid Andrade");
        aluno1.addNota(8.5);
        aluno1.addNota(9.0);
        aluno1.addNota(6.5);


        double mediaAluno1 = aluno1.calcMedia();

        System.out.printf("A media final do Aluno %s e: %.2f\n", aluno1.getNome(), mediaAluno1);
        if(aluno1.verficarAprovacao(MEDIA_MINIMA)){
            System.out.println("Situacao: APROVADO!\n");
        }else {
            System.out.println("Situacao: REPROVADO!\n");
        }

        System.out.println("-> Cadasto do Aluno <-");
        Aluno aluno2 = new Aluno("Deivid Andrade");
        aluno1.addNota(8.5);
        aluno1.addNota(9.0);
        aluno1.addNota(6.5);

        double mediaAluno2 = aluno2.calcMedia();

        System.out.printf("A media final do Aluno %s e: %.2f\n", aluno2.getNome(), mediaAluno2);
        if(aluno2.verficarAprovacao(MEDIA_MINIMA)){
            System.out.println("Situacao: APROVADO!\n");
        }else {
            System.out.println("Situacao: REPROVADO!\n");
        }

    }
}
