package ManipulacaoCSVProject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ManipulacaoCSV {



    private static final String CAMINHO_ARQUIVO = "C:\\CSV\\doacoes.csv";
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        exibirMenu();
        scanner.close(); 
    }

    private static void exibirMenu() {
        int opcao = 0;

        do {
            try {
                System.out.println("\nMenu:");
                System.out.println("1. Ler arquivo");
                System.out.println("2. Inserir nova doacao");
                System.out.println("3. Deletar doacao por codigo");
                System.out.println("4. Sair");
                System.out.print("Escolha uma opcao: ");

                String input = scanner.nextLine();

                if (!input.isEmpty() && input.matches("\\d+")) {
                    opcao = Integer.parseInt(input);

                    switch (opcao) {
                        case 1:
                            lerArquivo();
                            break;
                        case 2:
                            inserirNovaDoacao();
                            break;
                        case 3:
                            deletarDoacao();
                            break;
                        case 4:
                            System.out.println("Encerrando o programa.");
                            break;
                        default:
                            System.out.println("Opcao invalida. Tente novamente.");
                    }
                } else {
                    System.out.println("Opcao invalida. Tente novamente.");
                }

            } catch (java.util.InputMismatchException e) {
                System.err.println("Erro de entrada: Certifique-se de que a entrada está no formato correto.");
            } finally {
                scanner.nextLine();
            }
        } while (opcao != 4);
    
    }

    
    private static void lerArquivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } finally {
            exibirMenu();  
        }
    }

    private static void inserirNovaDoacao() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO, true))) {

            System.out.println("Informe os dados da nova doacao:");

            System.out.print("Codigo: ");
            int codigo = readInt();

            
            scanner.nextLine();

            System.out.print("Nome: ");
            String nome = scanner.nextLine();

            System.out.print("CPF: ");
            String cpf = scanner.nextLine();
            System.out.print("Data de Nascimento (YYYY-MM-DD): ");
            String dataNascimento = scanner.nextLine();
            System.out.print("Tipo Sanguíneo: ");
            String tipoSanguineo = scanner.nextLine();
            System.out.print("Quantidade em ml de sangue doado: ");
            int mlDoados = readInt();

            String novaDoacao = String.format("%d,%s,%s,%s,%s,%d",
                    codigo, nome, cpf, dataNascimento, tipoSanguineo, mlDoados);

            writer.newLine();
            writer.write(novaDoacao);

            System.out.println("Nova doacao inserida com sucesso.");

        } catch (IOException e) {
            System.err.println("Erro ao inserir nova doacao: " + e.getMessage());
        }
    }

    private static void deletarDoacao() {
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO));
             BufferedWriter writer = new BufferedWriter(new FileWriter("temp.csv"))) {

            System.out.print("Informe o codigo da doacao a ser deletada: ");

           
            if (scanner.hasNextInt()) {
                int codigoDeletar = scanner.nextInt();
                boolean encontrouDoacao = false; 

                String linha;
                while ((linha = reader.readLine()) != null) {
                 
                    if (linha.trim().isEmpty()) {
                        continue;
                    }

                    String[] campos = linha.split(",");
                    
                   
                    if (campos.length > 0) {
                        try {
                            int codigoAtual = Integer.parseInt(campos[0]);

                            if (codigoAtual == codigoDeletar) {
                                System.out.println("Doacao encontrada para deletar: " + linha);
                                encontrouDoacao = true;
                            } else {
                                writer.write(linha);
                                writer.newLine();
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Erro ao processar o codigo da doacao: " + e.getMessage());
                            System.err.println("Valor problematico: " + campos[0]);
                        }
                    }
                }

                if (!encontrouDoacao) {
                    System.out.println("Nenhuma doação encontrada com o codigo informado.");
                } else {
                    System.out.println("Doacao deletada com sucesso.");
                }

            } else {
                System.out.println("Entrada invalida. O código da doacao deve ser um numero inteiro.");
            }

        } catch (IOException e) {
            System.err.println("Erro ao deletar doacao: " + e.getMessage());
        }

      
        File arquivoOriginal = new File(CAMINHO_ARQUIVO);
        if (!arquivoOriginal.delete()) {
            System.err.println("Erro ao excluir o arquivo original antes de renomear o temporário.");
            return;
        }

        
        File arquivoTemp = new File("temp.csv");
        if (arquivoTemp.renameTo(arquivoOriginal)) {
            System.out.println("Arquivo atualizado com a doacao deletada.");
        } else {
            System.err.println("Erro ao renomear o arquivo temporario para o nome original.");
        }
    }




    private static int readInt() {
        while (true) {
            try {
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.err.println("Erro de entrada: Insira um número inteiro.");
                scanner.nextLine(); 
            }
        }
    }
}
