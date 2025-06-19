import java.io.*;
import java.util.Locale;
import java.util.Scanner;


public class SistemaEscolar {

    //Const para o nome do arquivo de dados
    private static final String NOME_ARQUIVO = "testando.csv";
    private static Turma turma = new Turma("Turma teste");

    public static void main(String[] args){
        Locale.setDefault(new Locale("pt", "BR"));
        Scanner scanner = new Scanner(System.in);


        // Carregamento dos dados salvos
        carregarDados();

        int opcao = -1;
        while (opcao != 0){
            exibirMenu();
            if (scanner.hasNextInt()){
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Opcao invalida. Por favor, digite um numero.");
                scanner.next();
                continue;
            }

            switch (opcao){
                case 1:
                    addAluno(scanner);
                    break;
                case 2:
                    lancarNotas(scanner);
                    break;
                case 3:
                    turma.listarAlunos();
                    break;
                case 4:
                    salvarDados();
                    break;
                case 5:
                    carregarDados();
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
        scanner.close();
    }

    // Menu
    private static void exibirMenu(){
        System.out.println("\n--- Sistema de Gestao de Turmas ---");
        System.out.println("1. Adicionar Aluno a Turma");
        System.out.println("2. Lancar Notas para um Aluno");
        System.out.println("3. Ver Relatorio Completo em Arquivo");
        System.out.println("4. Salvar Dados em Arquivo");
        System.out.println("5. Carregar Dados do Arquivo");
        System.out.println("0. Sair");
        System.out.println("Escolha uma opcao: ");
    }

    private static void addAluno(Scanner scanner){
        System.out.print("Digite o nome do aluno(a) para lancar notas: ");
        String nome = scanner.nextLine();
        Aluno novoAluno = new Aluno(nome);
        turma.addAluno(novoAluno);
        System.out.println("Aluno " + nome + " adicionado com sucesso!");
    }

    private static void lancarNotas(Scanner scanner){
        System.out.print("Digite o nome do aluno para lancar notas: ");
        String nome = scanner.nextLine();
        Aluno aluno = turma.buscarAluno(nome);

        if (aluno == null){
            System.out.println("Aluno nao encontrado");
            return;
        }

        System.out.println("Digite as notas para " + aluno.getNome() + " (digite uma letra para parar).");
        for (int i = 0; i < 3; i++){
            System.out.print("Digite a " + (i + 1) + "ª nota: ");
            if (scanner.hasNextDouble()){
                double nota = scanner.nextDouble();
                aluno.addNota(nota);
            } else {
                scanner.next();
                break;
            }
        }

        scanner.next(); //Limpar buffer
        System.out.println("Notas lancadas com sucesso!");
    }

    private static void salvarDados(){
        try (PrintWriter writer = new PrintWriter(new FileWriter(NOME_ARQUIVO))){
            for(Aluno aluno : turma.getAlunos()){
                writer.print(aluno.getNome());
                for (Double nota : aluno.getNotas()){
                    writer.print("," + nota);
                }
                writer.println();
            }
            System.out.println("Dados salvos com sucesso no arquivo " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dadors: " + e.getMessage());
        }
    }

    private static void carregarDados(){
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))){
            String linha;
            turma = new Turma ("5º Ano B");
            while ((linha = reader.readLine()) != null){
                String[] dados = linha.split(",");
                Aluno aluno = new Aluno(dados[0]);
                for (int i = 1; i < dados.length; i++){
                    aluno.addNota(Double.parseDouble(dados[i]));
                }
                turma.addAluno(aluno);
            }
            System.out.println("Dados carregados com sucesso do arquivo " + NOME_ARQUIVO);
        } catch (FileNotFoundException e){
            System.out.println("Arquivo de dados ainda não existe. Será criado ao salvar.");
        } catch (IOException e){
            System.out.println("Erro ao carregar os dados: " + e.getMessage());
        }
    }
}
