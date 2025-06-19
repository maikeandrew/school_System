import java.util.ArrayList;
import java.util.List;

public class Aluno {
    // Encapsulamento
    private String nome;
    private List<Double> notas;

    public Aluno(String nome){
        this.nome = nome;
        this.notas = new ArrayList<>();
    }

    // Adiciona Nota
    public void addNota(double nota){
        if(nota >= 0 && nota <= 10 ){
            this.notas.add(nota);
        }else{
            System.out.println("Nota invalida. Insira um valor entre 0 e 10.");
        }
    }

    // Calcula a media
    public double calcMedia(){
        if(notas.isEmpty()){
            return 0.0;
        }
        double soma = 0.0;
        for (double nota : this.notas){
            soma += nota;
        }
        return soma / this.notas.size();
    }


    // Verifica se foi aprovado ou nao aprovado
    public boolean verficarAprovacao(double mediaParaAprovar){
        double mediaFinal = calcMedia();
        return mediaFinal >= mediaParaAprovar;
    }

    public String getNome(){
        return this.nome;
    }

    public List<Double> getNotas(){
        return this.notas;
    }
}