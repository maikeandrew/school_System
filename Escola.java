
import java.util.Locale;
import java.util.Scanner;


public class Escola {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        final double MEDIA_MINIMA = 7.0;
        final int LIMITE_DE_NOTAS = 3;

        String cadastrarNovoAluno = "s";

        while (cadastrarNovoAluno.equalsIgnoreCase("s")){
            System.out.print("\nInforme o nome do aluno(a): ");
            String nomeAluno = scanner.nextLine();

            Aluno aluno = new Aluno(nomeAluno);

            System.out.println("<------------------------------------->");
            System.out.println("Digite as notas de " + aluno.getNome() + ".");
            System.out.println("Digite qualquer letra para finalizar");
            System.out.println("<-------------------------------------->");

            for(int i = 0; i < LIMITE_DE_NOTAS; i++){
                System.out.print("Digite a " + (i + 1) + "a nota (ou uma letra para parar): ");

                if (scanner.hasNextDouble()){
                    double nota = scanner.nextDouble();
                    aluno.addNota(nota);
                } else {
                    scanner.next();
                    System.out.println("Entrada de notas finalizadas pelo usuario.");
                    break;
                }
            }

            System.out.println("\n--- Resultado para " + aluno.getNome() + "---");
            double mediaFinal = aluno.calcMedia();
            System.out.printf("Media final: %.1f\n", mediaFinal);

            if(aluno.verficarAprovacao(MEDIA_MINIMA)){
                System.out.println("Sistuacao: APROVADO!");
            }else{
                System.out.println("Situacao: REPROVADO!");
            }
            System.out.print("-----------------------------------------------\n");

            System.out.print("Deseja cadastrar outro aluno? (s/n): ");
            cadastrarNovoAluno = scanner.next();

            scanner.nextLine();
        }
        System.out.println("\nPrograma Finalizado.");
        scanner.close();
    }
}
