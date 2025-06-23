import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        System.out.println("\n--- Sistema de Gestao de Turmas ---");
        System.out.println("1. Adicionar Aluno");
        System.out.println("2. Cadastrar Disciplina");
        System.out.println("3. Lancar Nota para um Aluno");
        System.out.println("4. Editar Nome de Aluno");
        System.out.println("5. Excluir Aluno");
        System.out.println("6. Gerenciar Notas de Aluno");
        System.out.println("7. Exibir Relatório da Turma");
        System.out.println("0. Sair");
    }

    public int obterOpcaoDoMenu() {
        System.out.print("Escolha uma opção: ");
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String obterEntradaDoUsuario(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public double obterNota() {
        while (true) {
            System.out.print("Digite a nota: ");
            String notaStr = scanner.nextLine().replace(',', '.');
            try {
                return Double.parseDouble(notaStr);
            } catch (NumberFormatException e) {
                exibirMensagemDeErro("Entrada inválida. Por favor, digite um número.");
            }
        }
    }

    public boolean confirmarAcao(String mensagemDeConfirmacao) {
        System.out.print(mensagemDeConfirmacao + " (S/N): ");
        String confirmacao = scanner.nextLine();
        return confirmacao.equalsIgnoreCase("S");
    }

    // Selecao de disciplina --------------------------------------------------
    public Disciplina selecionarDisciplina(List<Disciplina> disciplinas) {
        if (disciplinas.isEmpty()) {
            exibirMensagem("Este aluno não está matriculado em nenhuma disciplina.");
            return null;
        }

        System.out.println("Selecione a disciplina: ");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNome());
        }

        System.out.print("Opção: ");
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            if (opcao < 1 || opcao > disciplinas.size()) {
                exibirMensagemDeErro("Opção de disciplina inválida.");
                return null;
            }
            return disciplinas.get(opcao - 1);
        } catch (NumberFormatException e) {
            exibirMensagemDeErro("Opção inválida.");
            return null;
        }
    }

    // Exibe o relatorio da turma ----------------------------------------------------------------------
    public void exibirRelatorioDaTurma(Turma turma) {
        final double MEDIA_PARA_APROVACAO = 6.0;

        System.out.println("\n--- Relatorio da Turma: " + turma.getNomeDaTurma() + " ---");
        List<Aluno> alunos = turma.getAlunos();
        if (alunos.isEmpty()) {
            System.out.println("A turma ainda nao possui alunos cadastrados.");
            return;
        }

        for (Aluno aluno : alunos) {
            System.out.println("========================================");
            System.out.printf("Aluno: %s\n", aluno.getNome());
            System.out.println("----------------------------------------");
            List<Disciplina> disciplinas = aluno.getDisciplinas();
            if (disciplinas.isEmpty()) {
                System.out.println("  [Nenhuma disciplina matriculada]");
            } else {
                for (Disciplina disciplina : disciplinas) {
                    System.out.print(formatarLinhaDisciplina(disciplina, MEDIA_PARA_APROVACAO));
                }
            }
        }
        System.out.println("========================================");
        System.out.println("--- Fim do Relatório ---");
    }

    private String formatarLinhaDisciplina(Disciplina disciplina, double mediaParaAprovar) {
        String nomeDisciplina = disciplina.getNome();
        List<Double> notas = disciplina.getNotas();
        double media = disciplina.calcMedia();
        String situacao = disciplina.verificarAprovacao(mediaParaAprovar) ? "Aprovado" : "Reprovado";

        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  -> Disciplina: %-15s | Notas: ", nomeDisciplina));

        if (notas.isEmpty()) {
            sb.append("[Nenhuma nota lançada]");
        } else {
            for (int i = 0; i < notas.size(); i++) {
                sb.append(String.format("%.1f", notas.get(i)));
                if (i < notas.size() - 1) {
                    sb.append(" : ");
                }
            }
        }
        sb.append(String.format(" | Media: %.1f | Situacao: %s\n", media, situacao));
        return sb.toString();
    }

    // Gerencia acoes sobre as notas (Notas ja lancadas) --------------------------------------------------
    public int obterAcaoParaNota(Aluno aluno, Disciplina disciplina) {
        System.out.println("\nNotas atuais de " + aluno.getNome() + " em " + disciplina.getNome() + ":");
        List<Double> notas = disciplina.getNotas();

        if (notas.isEmpty()) {
            exibirMensagem("[Nenhuma nota lançada para esta disciplina]");
            return 0;
        }
        for (int i = 0; i < notas.size(); i++) {
            System.out.println("  " + (i + 1) + ": " + notas.get(i));
        }
        System.out.println("\nO que você deseja fazer?");
        System.out.println("1. Alterar uma nota");
        System.out.println("2. Remover uma nota");
        System.out.println("0. Voltar ao menu principal");
        return obterOpcaoDoMenu();
    }

    public int obterIndiceNota() {
        System.out.print("Qual o número da nota? ");
        try {
            return Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirMensagemDeErro(String erro) {
        System.out.println("Erro: " + erro);
    }

    public void fechar() {
        scanner.close();
    }
}