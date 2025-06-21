import java.io.*;
import java.util.Locale;
import java.util.List;
import java.util.Scanner;


public class SistemaEscolar {

    //Const para o nome do arquivo de dados
    private static final String NOME_ARQUIVO = "testando.csv";
    private static Turma turma = new Turma("Turma teste");

    public static void main(String[] args) {
        Locale.setDefault(new Locale("pt", "BR"));
        Scanner scanner = new Scanner(System.in);


        // Carregamento dos dados salvos
        carregarDados();

        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            if (scanner.hasNextInt()) {
                opcao = scanner.nextInt();
                scanner.nextLine();
            } else {
                System.out.println("Opcao invalida. Por favor, digite um numero.");
                scanner.next();
                continue;
            }

            switch (opcao) {
                case 1:
                    addAluno(scanner);
                    break;
                case 2:
                    cadastrarDisciplina(scanner);
                    break;
                case 3:
                    lancarNotas(scanner);
                    break;
                case 4:
                    editarNomeAluno(scanner);
                    break;
                case 5:
                    exibirRelatorioDaTurma();
                    break;
                case 6:
                    salvarDados();
                    break;
                case 7:
                    excluirAluno(scanner);
                    break;
                case 8:
                    gerenciarNotas(scanner);
                    break;
                case 0:
                    System.out.println("Salvando dados antes de sair...");
                    salvarDados();
                    System.out.println("Programa finalizado.");
                    break;
                default:
                    System.out.println("Opcao invalida. Tente novamente.");
            }
        }
        scanner.close();
    }

    // Menu
    private static void exibirMenu() {
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

    //Lancamentos das notas e tratamento de erros
    private static void lancarNotas(Scanner scanner) {
        System.out.print("Digite o nome do aluno para lancar notas: ");
        String nome = scanner.nextLine();
        Aluno aluno = turma.buscarAluno(nome);

        if (aluno == null) {
            System.out.println("Aluno nao encontrado");
            return;
        }

        List<Disciplina> disciplinasDoAluno = aluno.getDisciplinas();
        if (disciplinasDoAluno.isEmpty()) {
            System.out.println("Este aluno não está matriculado em nenhuma disciplina.");
            //System.out.println("Primeiro, cadastre disciplinas na turma e adicione o aluno novamente.");
            return;
        }

        System.out.println("selecione a disciplina para lancar a nota:");
        for (int i = 0; i < disciplinasDoAluno.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinasDoAluno.get(i).getNome());
        }
        System.out.print("Opcao: ");
        int opcaoDisciplina = scanner.nextInt();
        scanner.nextLine(); //Limpar buffer

        if (opcaoDisciplina < 1 || opcaoDisciplina > disciplinasDoAluno.size()) {
            System.out.println("Opcao de disciplina invalida.");
            return;
        }

        Disciplina disciplinaSelecionada = disciplinasDoAluno.get(opcaoDisciplina - 1);

        System.out.print("Digite a nota para " + disciplinaSelecionada.getNome() + ": ");
        if (scanner.hasNextInt()) {
            double nota = scanner.nextDouble();
            scanner.nextLine();

            if (disciplinaSelecionada.addNota(nota)) {
                System.out.println("Nota lancada com sucesso!");
            } else {
                System.out.println("Erro: Nota inválida. A nota deve ser entre 0.0 e 10.0.");
            }
        } else {
            System.out.println("Entrada inválida. Por favor, digite um número para a nota.");
            scanner.nextLine();
        }
    }

    //Gerenciamento das Notas dos alunos
    private static void gerenciarNotas(Scanner scanner) {
        // -> Encontrar Aluno
        System.out.print("Digite o nome do aluno para gerenciar as notas: ");
        String nomeAluno = scanner.nextLine();
        Aluno aluno = turma.buscarAluno(nomeAluno);
        if (aluno == null) {
            System.out.println("Erro: Aluno nao encontrado.");
            return;
        }

        // -> Encontrar a Disciplina
        if (aluno.getDisciplinas().isEmpty()) {
            System.out.println("Este aluno nao esta matriculado em nenhuma disciplina.");
            return;
        }
        System.out.println("Selecione a disciplina para gerenciar as notas: ");
        List<Disciplina> disciplinas = aluno.getDisciplinas();
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNome());
        }
        System.out.print("Opcao: ");
        int opcaoDisciplina = scanner.nextInt();
        scanner.nextLine();
        if (opcaoDisciplina < 1 || opcaoDisciplina > disciplinas.size()) {
            System.out.println("Opcao de disciplina invalida.");
            return;
        }

        Disciplina disciplinaSelecionada = disciplinas.get(opcaoDisciplina - 1);

        // -> Exibir as notas, Sub Menu
        System.out.println("\nNotas atuais de " + aluno.getNome() + " em " + disciplinaSelecionada.getNome() + ":");
        List<Double> notas = disciplinaSelecionada.getNotas();
        if (notas.isEmpty()) {
            System.out.println("[Nenhuma nota lancada para esta disciplina]");
            return;
        }
        for (int i = 0; i < notas.size(); i++) {
            System.out.println("  " + (i + 1) + ": " + notas.get(i));
        }

        System.out.println("\nO que voce deseja fazer?");
        System.out.println("1. Alterar uma nota");
        System.out.println("2. Remover uma nota");
        System.out.println("0. Voltar ao menu principal");
        System.out.println("Opcao: ");
        int opcaoAco = scanner.nextInt();
        scanner.nextLine();

        switch (opcaoAco) {
            case 1: //Para alterar a nota
                System.out.print("Qual o numero da nota que deseja alterar? ");
                int indiceAlterar = scanner.nextInt() - 1;
                System.out.print("Digite a nova nota: ");
                double novaNota = scanner.nextDouble();
                scanner.nextLine();

                if (disciplinaSelecionada.alterarNota(indiceAlterar, novaNota)) {
                    System.out.println("Nota alterada com sucesso!");
                } else {
                    System.out.println("Nao foi possivel alterar a nota. Verifique novamente se nota e valor sao validos");
                }
                break;
            case 2:
                // -> Para remover a nota
                System.out.println("Qual o numero da nota que deseja remover? ");
                int indiceRemover = scanner.nextInt() - 1;
                scanner.nextLine();

                if (disciplinaSelecionada.removerNota(indiceRemover)) {
                    System.out.println("Nota removida com sucesso!");
                } else {
                    System.out.println("Nao foi possivel remover a nota. Verifique se a nota e valor sao validos");
                }
                break;
            case 0:
                break;
            default:
                System.out.println("Opcao invalida.");
        }
    }


    // Salvamento dos dados
    private static void salvarDados() {
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
    private static void exibirRelatorioDaTurma() {
        final double MEDIA_PARA_APROVACAO = 6.0;

        System.out.println("\n--- Relatorio da Turma:" + turma.getNomeDaTurma() + " ---\n");

        List<Aluno> alunos = turma.getAlunos();

        if (alunos.isEmpty()) {
            System.out.println("A turma ainda nao possui alunos cadastrados.");
            return;
        }

        for (Aluno aluno : alunos) {
            System.out.printf("\n========================================");
            System.out.printf("Aluno: " + aluno.getNome());
            System.out.printf("----------------------------------------");

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
