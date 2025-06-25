import java.util.List;
import java.util.Scanner;

public class ConsoleView {

    private final Scanner scanner;

    public ConsoleView() {
        this.scanner = new Scanner(System.in);
    }

    public void exibirMenu() {
        System.out.println("\n--- Sistema de Gestao Escolar ---");
        System.out.println("1. Adicionar Aluno");
        System.out.println("2. Cadastrar Disciplina");
        System.out.println("3. Lançar Nota (Trabalho/Prova)");
        System.out.println("4. Exibir Relatório Detalhado da Turma");
        System.out.println("5. Editar Nome de Aluno");
        System.out.println("6. Excluir Aluno");
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

    public int selecionarBimestre() {
        System.out.println("Para qual bimestre deseja lançar a nota?");
        System.out.println("1. 1º Bimestre");
        System.out.println("2. 2º Bimestre");
        System.out.println("3. 3º Bimestre");
        System.out.println("4. 4º Bimestre");
        return obterOpcaoDoMenu();
    }

    public int selecionarTipoDeNota() {
        System.out.println("Qual o tipo de nota?");
        System.out.println("1. Nota de Trabalho (pode adicionar várias)");
        System.out.println("2. Nota da Prova (substitui a anterior, se houver)");
        return obterOpcaoDoMenu();
    }

    public String obterEntradaDoUsuario(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public double obterNota(String tipo) {
        while (true) {
            System.out.print("Digite a nota: " + tipo + ": ");
            String notaStr = scanner.nextLine().replace(',', '.');
            try {
                double nota = Double.parseDouble(notaStr);
                if (nota >= 0 && nota <= 10) {
                    return nota;
                } else {
                    exibirMensagemDeErro("A nota deve ser entre 0.0 e 10.0.");
                }
            } catch (NumberFormatException e) {
                exibirMensagemDeErro("Entrada inválida. Por favor, digite um número.");
            }
        }
    }

    // Selecao de disciplina
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

    public boolean confirmarAcao(String mensagemDeConfirmacao) {
        System.out.print(mensagemDeConfirmacao + " (S/N): ");
        return scanner.nextLine().equalsIgnoreCase("S");
    }

    public void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    public void exibirMensagemDeErro(String erro) {
        System.out.println("Erro: " + erro);
    }

    // Exibe o relatorio da turma
    public void exibirRelatorioDaTurma(Turma turma, double mediaAnualParaAprovacao, double mediaBimestreParaAprovacao) {

        System.out.println("\n--- Relatório Detalhado da Turma: " + turma.getNomeDaTurma() + " ---");
        List<Aluno> alunos = turma.getAlunos();
        if (alunos.isEmpty()) {
            exibirMensagem("A turma ainda não possui alunos cadastrados.");
            return;
        }

        for (Aluno aluno : alunos) {
            System.out.println("\n==========================================================================");
            System.out.printf("Aluno: %s\n", aluno.getNome());

            List<Disciplina> disciplinas = aluno.getDisciplinas();
            if (disciplinas.isEmpty()) {
                System.out.println("  [Nenhuma disciplina matriculada]");
            } else {
                for (Disciplina disciplina : disciplinas) {
                    System.out.println("--------------------------------------------------------------------------");
                    String situacaoFinal = disciplina.verificarAprovacaoFinal(mediaAnualParaAprovacao) ? "Aprovado" : "Reprovado";
                    System.out.printf("  -> Disciplina: %s (Média Final Anual: %.1f) - Situação Final: %s\n",
                            disciplina.getNome(),
                            disciplina.calcularMediaFinalAnual(),
                            situacaoFinal);
                    System.out.println("..........................................................................");

                    for (int i = 0; i < disciplina.getBimestres().size(); i++) {
                        Bimestre bimestre = disciplina.getBimestres().get(i);
                        String situacaoBimestre = bimestre.verificarAprovacaoBimestre(mediaBimestreParaAprovacao) ? "Ok" : "Recuperação";
                        System.out.printf("     %dº Bimestre: [Trabalhos: %s (Média: %.1f)] [Prova: %.1f] -> Media: %.1f (%s)\n",
                                (i + 1),
                                formatarNotasTrabalho(bimestre.getNotasTrabalhos()),
                                bimestre.calcularMediaTrabalhos(),
                                bimestre.getNotaProva(),
                                bimestre.calcularNotaBimestre(),
                                situacaoBimestre
                        );
                    }
                }
            }
        }
        System.out.println("==========================================================================");
        System.out.println("--- Fim do Relatório ---");
    }

    private String formatarNotasTrabalho(List<Double> notas) {
        if (notas.isEmpty()) {
            return "N/A";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < notas.size(); i++) {
            sb.append(String.format("%.1f", notas.get(i)));
            if (i < notas.size() - 1) {
                sb.append(" + ");
            }
        }
        return sb.toString();
    }

    public void fechar() {
        scanner.close();
    }
}