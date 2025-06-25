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
                exibirRelatorioDaTurma();
                break;
            case 5:
                editarNomeAluno();
                break;
            case 6:
                excluirAluno();
                break;
            case 0:
                sair();
                break;
            default:
                view.exibirMensagemDeErro("Opção inválida.");
                break;
        }
    }

    //lancando as Notas
    private void lancarNota() {
        String nomeAluno = view.obterEntradaDoUsuario("Digite o nome do aluno: ");
        Aluno aluno = turma.buscarAluno(nomeAluno);
        if (aluno == null) {
            view.exibirMensagemDeErro("Aluno não encontrado.");
            return;
        }

        Disciplina disciplina = view.selecionarDisciplina(aluno.getDisciplinas());
        if (disciplina == null) return;

        int numBimestre = view.selecionarBimestre();
        Bimestre bimestre = disciplina.getBimestre(numBimestre);
        if (bimestre == null) {
            view.exibirMensagemDeErro("Bimestre inválido.");
            return;
        }

        int tipoNota = view.selecionarTipoDeNota();
        if (tipoNota == 1) { // Para trabalho
            double notaTrabalho = view.obterNota("Trabalho");
            bimestre.adicionarNotaTrabalho(notaTrabalho);
            view.exibirMensagem("Nota de trabalho adicionada com sucesso!");
        } else if (tipoNota == 2) { // Para provas
            double notaProva = view.obterNota("Prova");
            bimestre.setNotaProva(notaProva);
            view.exibirMensagem("Nota da prova definida com sucesso!");
        } else {
            view.exibirMensagemDeErro("Tipo de nota inválido.");
        }
    }


    //Salvando os Dados
    private void salvarDados() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Aluno aluno : turma.getAlunos()) {
                for (Disciplina disciplina : aluno.getDisciplinas()) {
                    StringBuilder linha = new StringBuilder();
                    linha.append(aluno.getNome()).append(";").append(disciplina.getNome());

                    for (int i = 0; i < disciplina.getBimestres().size(); i++) {
                        Bimestre bimestre = disciplina.getBimestres().get(i);
                        linha.append(";B").append(i + 1);

                        linha.append(";T"); //-> Salva notas dos trabalhos
                        for (double notaT : bimestre.getNotasTrabalhos()) {
                            linha.append(":").append(notaT);
                        }

                        linha.append(";P:").append(bimestre.getNotaProva()); //Salva notas das provas
                    }
                    writer.println(linha.toString());
                }
            }
        } catch (IOException e) {
            view.exibirMensagemDeErro("Falha ao salvar os dados: " + e.getMessage());
        }
    }


    //Carregamento dos dados
    private void carregarDados() {
        File arquivo = new File(NOME_ARQUIVO);
        if (!arquivo.exists()) {
            view.exibirMensagem("Arquivo de dados ainda não existe. Será criado ao salvar.");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            this.turma = new Turma("Turma teste");

            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;

                String[] partes = linha.split(";");
                String nomeAluno = partes[0];
                String nomeDisciplina = partes[1];

                Aluno aluno = turma.buscarAluno(nomeAluno);
                if (aluno == null) {
                    aluno = new Aluno(nomeAluno);
                    turma.addAluno(aluno); //Add aluno a turma
                }

                Disciplina disciplina = aluno.buscarDisciplina(nomeDisciplina);
                if (disciplina == null) {
                    disciplina = new Disciplina(nomeDisciplina);
                    aluno.addDisciplina(disciplina);
                }

                //Processa os dados dos bimestres
                for (int i = 2; i < partes.length; i += 3) {
                    int numBimestre = Integer.parseInt(partes[i].substring(1));
                    Bimestre bimestre = disciplina.getBimestre(numBimestre);

                    String[] notasT = partes[i + 1].split(":");
                    for (int j = 1; j < notasT.length; j++) {
                        bimestre.adicionarNotaTrabalho(Double.parseDouble(notasT[j]));
                    }

                    String[] notaP = partes[i + 2].split(":");
                    if (notaP.length > 1) {
                        bimestre.setNotaProva(Double.parseDouble(notaP[1]));
                    }
                }
            }
            view.exibirMensagem("Dados carregados com sucesso.");
        } catch (IOException | NumberFormatException | ArrayIndexOutOfBoundsException e) {
            view.exibirMensagemDeErro("Falha crítica ao carregar os dados. Verifique o arquivo '" + NOME_ARQUIVO + "'. " + e.getMessage());
        }
    }


    //Cadastrar Alunos, Disciplinas e Notas
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

    //Gerenciar dados/Alterar e remover (Aluno, Notas)
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

    //Exibir Relatorio, Salvar e Carregar Dados
    private void exibirRelatorioDaTurma() {
        double mediaAnual = turma.getMediaParaAprovacao();
        double mediaBimestre = turma.getMediaBimestreParaAprovacao();

        view.exibirRelatorioDaTurma(this.turma, mediaAnual, mediaBimestre);
    }

    private void sair() {
        view.exibirMensagem("Salvando dados...");
        salvarDados();
        view.exibirMensagem("Programa finalizado.");
        this.executando = false;
    }
}
