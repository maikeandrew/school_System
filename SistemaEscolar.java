import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;


public class SistemaEscolar {

    //Const para o nome do arquivo de dados
    private static final String NOME_ARQUIVO = "testando.csv";
    private static Turma turma = new Turma("Turma teste");

    private static final Map<Integer, Consumer<Scanner>> ACOES_DO_MENU = new HashMap<>();

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner scanner = new Scanner(System.in);

        configurarAcoes();
        carregarDados();

        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();

            try {
                opcao = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Por favor, digite um número.");
                continue;
            }
            Consumer<Scanner> acao = ACOES_DO_MENU.getOrDefault(opcao, SistemaEscolar::acaoInvalida);
            acao.accept(scanner);

            if (opcao == 0){ break; }
        }
        scanner.close();
    }

    private static void configurarAcoes(){
        ACOES_DO_MENU.put(1, SistemaEscolar::addAluno);
        ACOES_DO_MENU.put(2, SistemaEscolar::cadastrarDisciplina);
        ACOES_DO_MENU.put(3, SistemaEscolar::lancarNotas);
        ACOES_DO_MENU.put(4, SistemaEscolar::editarNomeAluno);
        ACOES_DO_MENU.put(5, SistemaEscolar::exibirRelatorioDaTurma);
        ACOES_DO_MENU.put(6, SistemaEscolar::salvarDados);
        ACOES_DO_MENU.put(7, SistemaEscolar::excluirAluno);
        ACOES_DO_MENU.put(8, SistemaEscolar::gerenciarNotas);

        ACOES_DO_MENU.put(0, scanner -> { //Acao de sair
            System.out.println("Salvando dados antes de sair...");
            salvarDados(scanner);
            System.out.println("Programa finalizado.");
        });
    }

    private static void exibirMenu(){
        System.out.println("\n--- Sistema de Gestao de Turmas ---");
        System.out.println("1. Adicionar Aluno");
        System.out.println("2. Cadastrar Discilpina");
        System.out.println("3. Lancar Notas para um Aluno");
        System.out.println("4. Editar dados do Aluno");
        System.out.println("5. Ver Relatorio Completo em Arquivo");
        System.out.println("6. Salvar Dados em Arquivo");
        System.out.println("7. Excluie Aluno");
        System.out.println("8. Gerenciar Notas de Aluno");
        System.out.println("0. Sair");
        System.out.println("Escolha uma opcao: ");
    }

    private static void acaoInvalida(Scanner scanner){
        System.out.println("Opção inválida. Tente novamente.");
    }

//---------------------------------------------------------------------------------------------------------------------


    //Cadastro de alunos
    private static void addAluno(Scanner scanner) {
        System.out.print("Digite o nome do aluno(a) para lancar notas: ");
        String nome = scanner.nextLine();
        Aluno novoAluno = new Aluno(nome);
        turma.addAluno(novoAluno);
        System.out.println("Aluno " + nome + " adicionado com sucesso!");
    }

    //Editar nome dados do aluno
    private static void editarNomeAluno(Scanner scanner) {
        System.out.print("Digite o nome ATUAL do aluno que deseja editar: ");
        String nomeAtual = scanner.nextLine();

        Aluno alunoParaEditar = turma.buscarAluno(nomeAtual);

        if (alunoParaEditar == null) {
            System.out.println("Erro: Aluno nao encontrado.");
            return;
        }

        System.out.print("Digite o NOVO nome para o aluno '" + nomeAtual + "': ");
        String novoNome = scanner.nextLine();

        //Valida o nome para nao existir nome em branco
        if (novoNome.trim().isEmpty()) {
            System.out.println("Erro: O novo nome nao pode ser vazio.");
            return;
        }

        alunoParaEditar.setNome(novoNome);

        System.out.println("Nome do aluno alterado com sucesso!");
    }

    //Excluir Aluno
    private static void excluirAluno(Scanner scanner) {
        System.out.print("Digite o nome do aluno que deseja excluir: ");
        String nome = scanner.nextLine();

        //Verifica se o aluno existe antes de pedir confirmacao
        if (turma.buscarAluno(nome) == null) {
            System.out.print("Erro: Aluno nao encontrado.");
            return;
        }

        System.out.print("Tem certeza que deseja excluir o aluno '" + nome + "'? Esta acao nao pode ser desfeita. (S/N): ");
        String confirmacao = scanner.nextLine();

        if (confirmacao.equalsIgnoreCase("S")) {
            boolean removido = turma.removerAluno(nome);
            if (removido) {
                System.out.println("Aluno removido com sucesso!");
            } else {
                System.out.println("Nao foi possivel remover o aluno.");
            }
        } else {
            System.out.println("Operacao de exclusao cancelada.");
        }
    }

    //Cadastro da disciplina
    private static void cadastrarDisciplina(Scanner scanner) {
        System.out.print("Digite o nome da nova disciplina a ser adicionada na turma: ");
        String nomeDisciplina = scanner.nextLine();
        turma.addDisciplinaAoCurriculo(nomeDisciplina);
        System.out.println("Disciplina '" + nomeDisciplina + "' cadastrada no curriculo da turma.");
    }

//<============= Lancamentos das notas ================================================================================
    private static void lancarNotas(Scanner scanner) {
        System.out.print("Digite o nome do aluno para lancar notas: ");
        String nome = scanner.nextLine();
        Aluno aluno = turma.buscarAluno(nome);

        if (aluno == null) {
            System.out.println("Aluno nao encontrado");
            return;
        }

        Disciplina disciplina = selecionarDisciplinaDoAluno(scanner, aluno);
        if (disciplina == null) return;

        System.out.print("Digite a nota para " + disciplina.getNome() + ": ");
        String notaStr = scanner.nextLine().replace(',', '.');

        try{
            double nota = Double.parseDouble(notaStr);
            if (disciplina.addNota(nota)){
                System.out.println("Nota lancada com sucesso!");
            } else {
                System.out.println("Erro: Nota inválida. A nota deve ser entre 0.0 e 10.0.");
            }
        } catch (NumberFormatException e){
            System.out.println("Entrada inválida. Por favor, digite um número válido para a nota.");
        }
    }
//====================================================================================================================>

//<============== Gerenciamento das Notas dos alunos ==================================================================
    private static void gerenciarNotas(Scanner scanner) {
        Aluno aluno = encontrarAlunoParaGerenciamento(scanner); // -> Encontrar Aluno
        if (aluno == null) return;

        Disciplina disciplina = selecionarDisciplinaDoAluno(scanner, aluno);// -> Encontrar a Disciplina
        if (disciplina == null) return;

        executarAcaoDeNota(scanner, aluno, disciplina);
    }

    private static Aluno encontrarAlunoParaGerenciamento(Scanner scanner){
        System.out.print("Digite o nome do aluno para gerenciar as notas: ");
        String nomeAluno = scanner.nextLine();
        Aluno aluno = turma.buscarAluno(nomeAluno);
        if (aluno == null) {
            System.out.println("Erro: Aluno não encontrado.");
            return null;
        }
        return aluno;
    }

    // Seleciona as disciplinas
    private static Disciplina selecionarDisciplinaDoAluno(Scanner scanner, Aluno aluno){
        if (aluno.getDisciplinas().isEmpty()){
            System.out.println("Este aluno não está matriculado em nenhuma disciplina.");
            return null;
        }

        System.out.println("Selecione a disciplina para gerenciar as notas:");
        List<Disciplina> disciplinas = aluno.getDisciplinas();
        for (int i = 0; i < disciplinas.size(); i++){
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNome());
        }

        System.out.print("Opção: ");
        int opcaoDisciplina = scanner.nextInt();
        scanner.nextLine();

        if (opcaoDisciplina < 1 || opcaoDisciplina > disciplinas.size()){
            System.out.println("Opção de disciplina inválida.");
            return null;
        }
        return disciplinas.get(opcaoDisciplina - 1);
    }

    // Gerencia a execucao das acoes sobre as notas (Menu)
    private static void executarAcaoDeNota(Scanner scanner, Aluno aluno, Disciplina disciplina){
        System.out.println("\nNotas atuais de " + aluno.getNome() + " em " + disciplina.getNome() + ":");
        List<Double> notas = disciplina.getNotas();
        if (notas.isEmpty()){
            System.out.println("[Nenhuma nota lançada para esta disciplina]");
            return;
        }
        for (int i =0; i < notas.size(); i++){
            System.out.println("  " + (i + 1) + ": " + notas.get(i));
        }

        System.out.println("\nO que você deseja fazer?");
        System.out.println("1. Alterar uma nota");
        System.out.println("2. Remover uma nota");
        System.out.println("0. Voltar ao menu principal");
        System.out.println("Opção: ");
        int opcaoAcao = scanner.nextInt();
        scanner.nextLine();

        switch (opcaoAcao){
            case 1:
                executarAlteracaoDeNota(scanner, disciplina);
                break;
            case 2:
                executarRemocaoDeNota(scanner, disciplina);
                break;
            case 0:
                break;
            default:
                System.out.println("Opção inválida.");
        }
    }

    // Alteracao das notas
    private static void executarAlteracaoDeNota(Scanner scanner, Disciplina disciplina){
        System.out.print("De qual bimestre que deseja alterar a nota? ");
        int indiceAlterar;
        try{
            indiceAlterar = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            return;
        }

        System.out.print("Digite a nova nota: ");
        String notaStr = scanner.nextLine().replace(',', '.');
        try {
            double novaNota = Double.parseDouble(notaStr);
            if (disciplina.alterarNota(indiceAlterar, novaNota)){
                System.out.println("Nota alterada com sucesso!");
            } else {
                System.out.println("Não foi possível alterar a nota. Verifique se o número da nota e o valor são válidos.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número válido para a nota.");
        }
    }

    // Remocao das Notas
    private static void executarRemocaoDeNota(Scanner scanner, Disciplina disciplina){
        System.out.print("Qual o número da nota que deseja remover? ");
        int indiceRemover;
        try {
            indiceRemover = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Por favor, digite um número.");
            return;
        }

        if (disciplina.removerNota(indiceRemover)){
            System.out.println("Nota removida com sucesso!");
        } else {
            System.out.println("Não foi possível remover a nota. Verifique se o número da nota é válido.");
        }
    }
// ====================================================================================================================>


    // Salvamento dos dados
    private static void salvarDados(Scanner scanner) {

        try (PrintWriter writer = new PrintWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Aluno aluno : turma.getAlunos()) {
                StringBuilder linha = new StringBuilder();
                linha.append(aluno.getNome()); //Add o nome do aluno

                for (Disciplina disciplina : aluno.getDisciplinas()) {
                    linha.append(";");
                    linha.append(disciplina.getNome());

                    for (Double nota : disciplina.getNotas()) {
                        linha.append(":").append(nota);
                    }
                }
                writer.println(linha.toString());
            }
            System.out.println("Dados salvos com sucesso no arquivo " + NOME_ARQUIVO);
        } catch (IOException e) {
            System.out.println("Erro ao salvar os dados: " + e.getMessage());
        }
    }

    //Processo de carregar os dados
    private static void carregarDados() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            turma = new Turma("Turma de teste");

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split(";");
                Aluno aluno = new Aluno(partes[0]);

                for (int i = 1; i < partes.length; i++) {
                    String[] dadosDisciplina = partes[i].split(":");
                    String nomeDisciplina = dadosDisciplina[0];

                    turma.addDisciplinaAoCurriculo(nomeDisciplina);

                    Disciplina disciplina = new Disciplina(nomeDisciplina);

                    for (int j = 1; j < dadosDisciplina.length; j++) {
                        disciplina.addNota(Double.parseDouble(dadosDisciplina[j]));
                    }
                    aluno.addDisciplina(disciplina);
                }
                turma.addAlunoJaExistente(aluno);
            }
            System.out.println("Dados carregados com sucesso do arquivo " + NOME_ARQUIVO);
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de dados ainda não existe. Será criado ao salvar.");
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar os dados: " + e.getMessage());
        }
    }

    // Exibe o relatorio da turma
    private static void exibirRelatorioDaTurma(Scanner scanner) {
        final double MEDIA_PARA_APROVACAO = 6.0;

        System.out.println("\n--- Relatorio da Turma:" + turma.getNomeDaTurma() + " ---\n");

        List<Aluno> alunos = turma.getAlunos();

        if (alunos.isEmpty()) {
            System.out.println("A turma ainda nao possui alunos cadastrados.");
            return;
        }

        for (Aluno aluno : alunos) {
            System.out.println("========================================");
            System.out.printf("Aluno: " + aluno.getNome());
            System.out.println("----------------------------------------");

            List<Disciplina> disciplinas = aluno.getDisciplinas();
            if (disciplinas.isEmpty()) {
                System.out.print(" [Nenhuma disciplina matriculada]");
            } else {
                for (Disciplina disciplina : disciplinas) {
                    String nomeDisciplina = disciplina.getNome();
                    List<Double> notas = disciplina.getNotas();
                    double media = disciplina.calcMedia();
                    String situacao = disciplina.verificarAprovacao(MEDIA_PARA_APROVACAO) ? "Aprovado" : "Reprovado";

                    System.out.printf("\n -> Disciplina: %-15s | Notas: ", nomeDisciplina);

                    if (notas.isEmpty()) {
                        System.out.print("[Nenhuma nota lancada]");
                    } else {
                        for (int i = 0; i < notas.size(); i++) {
                            System.out.printf("%.1f ", notas.get(i));

                            if (i < notas.size() - 1) {
                                System.out.print(" : ");
                            }
                        }
                    }
                    System.out.printf(" | Media: %.1f | Situação: %s\n", media, situacao);
                }
            }
        }
        System.out.println("========================================");
        System.out.println("--- Fim do Relatório ---");
    }

}
