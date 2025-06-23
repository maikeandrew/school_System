import java.io.*;

// Aqui esta todo o gerenciamento das aplicacoes do sistema

public class Aplicacao {

    private static final String NOME_ARQUIVO = "testando.csv";

    private final ConsoleView view;
    private Turma turma;
    private boolean executando;

    public Aplicacao() {
        this.view = new ConsoleView();
        this.turma = new Turma("Turma Tester");
        this.executando = true;
    }

    public void executar() {
        carregarDados();
        while (executando) {
            view.exibirMenu();
            int opcao = view.obterOpcaoDoMenu();
            processarOpcao(opcao);
        }
        view.fechar();
    }

    private void processarOpcao(int opcao) {
        switch (opcao) {
            case 1:
                addAluno();
                break;
            case 2:
                cadastrarDisciplina();
                break;
            case 3:
                lancarNota();
                break;
            case 4:
                editarNomeAluno();
                break;
            case 5:
                excluirAluno();
                break;
            case 6:
                gerenciarNotas();
                break;
            case 7:
                exibirRelatorioDaTurma();
                break;
            case 0:
                sair();
                break;
            default:
                view.exibirMensagemDeErro("Opção inválida. Tente novamente.");
                break;
        }
    }

    //Cadastrar Alunos, Disciplinas e Notas -------------------------------------------------------------
    private void addAluno() {
        String nome = view.obterEntradaDoUsuario("Digite o nome do novo aluno: ");
        if (nome.trim().isEmpty()) {
            view.exibirMensagemDeErro("O nome não pode ser vazio.");
            return;
        }
        turma.addAluno(new Aluno(nome));
        view.exibirMensagem("Aluno '" + nome + "' adicionado com sucesso!");
    }

    private void cadastrarDisciplina() {
        String nome = view.obterEntradaDoUsuario("Digite o nome da nova disciplina: ");
        if (nome.trim().isEmpty()) {
            view.exibirMensagemDeErro("O nome da disciplina não pode ser vazio.");
            return;
        }
        turma.addDisciplinaAoCurriculo(nome);
        view.exibirMensagem("Disciplina '" + nome + "' cadastrada no currículo da turma.");
    }

    private void lancarNota() {
        String nomeAluno = view.obterEntradaDoUsuario("Digite o nome do aluno: ");
        Aluno aluno = turma.buscarAluno(nomeAluno);
        if (aluno == null) {
            view.exibirMensagemDeErro("Aluno não encontrado.");
            return;
        }

        Disciplina disciplina = view.selecionarDisciplina(aluno.getDisciplinas());
        if (disciplina == null) return;

        double nota = view.obterNota();
        if (disciplina.addNota(nota)) {
            view.exibirMensagem("Nota lançada com sucesso!");
        } else {
            view.exibirMensagemDeErro("A nota deve ser entre 0.0 e 10.0.");
        }
    }

    //Gerenciar dados/Alterar e remover (Aluno, Notas) -------------------------------------------------------------
    private void editarNomeAluno() {
        String nomeAtual = view.obterEntradaDoUsuario("Digite o nome ATUAL do aluno: ");
        Aluno aluno = turma.buscarAluno(nomeAtual);
        if (aluno == null) {
            view.exibirMensagemDeErro("Aluno não encontrado.");
            return;
        }

        String novoNome = view.obterEntradaDoUsuario("Digite o NOVO nome para o aluno '" + nomeAtual + "': ");
        if (novoNome.trim().isEmpty()) {
            view.exibirMensagemDeErro("O nome não pode ser vazio.");
            return;
        }
        aluno.setNome(novoNome);
        view.exibirMensagem("Nome alterado com sucesso!");
    }

    private void excluirAluno() {
        String nome = view.obterEntradaDoUsuario("Digite o nome do aluno a ser excluído: ");
        Aluno aluno = turma.buscarAluno(nome);
        if (aluno == null) {
            view.exibirMensagemDeErro("Aluno não encontrado.");
            return;
        }

        boolean confirmado = view.confirmarAcao("Tem certeza que deseja excluir o aluno '" + nome + "'? Esta ação não pode ser desfeita.");
        if (confirmado) {
            if (turma.removerAluno(nome)) {
                view.exibirMensagem("Aluno removido com sucesso!");
            } else {
                view.exibirMensagemDeErro("Falha ao remover o aluno.");
            }
        } else {
            view.exibirMensagem("Exclusão cancelada.");
        }
    }

    private void gerenciarNotas() {
        String nomeAluno = view.obterEntradaDoUsuario("Digite o nome do aluno: ");
        Aluno aluno = turma.buscarAluno(nomeAluno);
        if (aluno == null) {
            view.exibirMensagemDeErro("Aluno não encontrado.");
            return;
        }

        Disciplina disciplina = view.selecionarDisciplina(aluno.getDisciplinas());
        if (disciplina == null) return;

        int acao = view.obterAcaoParaNota(aluno, disciplina);

        if (acao == 1) { // -> Alterar <-
            int indice = view.obterIndiceNota();
            double novaNota = view.obterNota();
            if (disciplina.alterarNota(indice, novaNota)) {
                view.exibirMensagem("Nota alterada com sucesso!");
            } else {
                view.exibirMensagemDeErro("Não foi possível alterar. Verifique se o bimestre da nota e o valor são válidos.");
            }
        } else if (acao == 2) { // -> Remover <-
            int indice = view.obterIndiceNota();
            if (disciplina.removerNota(indice)) {
                view.exibirMensagem("Nota removida com sucesso!");
            } else {
                view.exibirMensagemDeErro("Não foi possível remover. Verifique o número da nota.");
            }
        }
    }

    //Exibir Relatorio, Salvar e Carregar Dados ------------------------------------------------------------------
    private void exibirRelatorioDaTurma() {
        view.exibirRelatorioDaTurma(this.turma);
    }

    private void sair() {
        view.exibirMensagem("Salvando dados antes de sair...");
        salvarDados();
        view.exibirMensagem("Programa finalizado.");
        this.executando = false;
    }

    private void salvarDados() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Aluno aluno : turma.getAlunos()) {
                StringBuilder linha = new StringBuilder();
                linha.append(aluno.getNome());
                for (Disciplina disciplina : aluno.getDisciplinas()) {
                    linha.append(";").append(disciplina.getNome());
                    for (Double nota : disciplina.getNotas()) {
                        linha.append(":").append(nota);
                    }
                }
                writer.println(linha.toString());
            }
        } catch (IOException e) {
            view.exibirMensagemDeErro("Falha ao salvar os dados: " + e.getMessage());
        }
    }

    private void carregarDados() {
        try (BufferedReader reader = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            this.turma = new Turma("Turma de teste");
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                Aluno aluno = new Aluno(partes[0]);
                for (int i = 1; i < partes.length; i++) {
                    String[] dadosDisciplina = partes[i].split(":");
                    String nomeDisciplina = dadosDisciplina[0];
                    this.turma.addDisciplinaAoCurriculo(nomeDisciplina);
                    Disciplina disciplina = new Disciplina(nomeDisciplina);
                    for (int j = 1; j < dadosDisciplina.length; j++) {
                        disciplina.addNota(Double.parseDouble(dadosDisciplina[j].replace(',', '.')));
                    }
                    aluno.addDisciplina(disciplina);
                }
                this.turma.addAlunoJaExistente(aluno);
            }
            view.exibirMensagem("Dados carregados com sucesso.");
        } catch (FileNotFoundException e) {
            view.exibirMensagem("Arquivo de dados ainda não existe. Será criado ao salvar.");
        } catch (IOException | NumberFormatException e) {
            view.exibirMensagemDeErro("Falha ao carregar os dados: " + e.getMessage());
        }
    }

}
