
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class ResolucaoLabirinto {
    public static void main(String[] args) {
        try {
            Scanner input = new Scanner(System.in);
            System.out.print("Digite o nome do arquivo: ");
            String nomeArquivo = input.nextLine().trim();

            File arquivo = new File(nomeArquivo);
            if (!arquivo.exists() || !arquivo.isFile()) {
                System.out.println("Erro: Arquivo não encontrado ou inválido.");
                return;
            }

            Labirinto lab;
            try {
                lab = new Labirinto(nomeArquivo);
            } catch (IllegalArgumentException e) {
                System.out.println("Erro no formato do arquivo: " + e.getMessage());
                return;
            }

            Pilha<Coordenada> caminho = new Pilha<>(lab.linhas * lab.colunas);
            Pilha<Fila<Coordenada>> possibilidades = new Pilha<>(lab.linhas * lab.colunas);

            Coordenada atual = encontrarEntrada(lab);
            if (atual == null) {
                System.out.println("Erro: Entrada não encontrada nas bordas.");
                return;
            }

            boolean achouSaida = false;
            while (true) {
                Fila<Coordenada> fila = new Fila<>(3);
                adicionarPossibilidades(lab, atual, fila);

                if (fila.isEmpty()) {
                    // Modo regressivo
                    if (caminho.isEmpty()) {
                        System.out.println("Sem caminho possível.");
                        break;
                    }
                    atual = caminho.pop();
                    lab.labirinto[atual.getLinha()][atual.getColuna()] = ' ';
                    possibilidades.pop();
                } else {
                    // Modo progressivo
                    atual = fila.remove();
                    if (lab.labirinto[atual.getLinha()][atual.getColuna()] == 'S') {
                        achouSaida = true;
                        break;
                    }
                    lab.labirinto[atual.getLinha()][atual.getColuna()] = '*';
                    caminho.push(atual);
                    possibilidades.push(fila);
                }
            }

            if (achouSaida) {
                lab.mostrar();
                Pilha<Coordenada> inverso = new Pilha<>(lab.linhas * lab.colunas);
                while (!caminho.isEmpty()) inverso.push(caminho.pop());

                System.out.println("Caminho da entrada até a saída:");
                while (!inverso.isEmpty()) {
                    System.out.print(inverso.pop() + " ");
                }
                System.out.println();
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar o arquivo: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Erro inesperado: " + e.getMessage());
        }
    }

    static Coordenada encontrarEntrada(Labirinto lab) {
        // Procura 'E' nas bordas
        for (int i = 0; i < lab.linhas; i++) {
            if (lab.labirinto[i][0] == 'E') return new Coordenada(i, 0);
            if (lab.labirinto[i][lab.colunas - 1] == 'E') return new Coordenada(i, lab.colunas - 1);
        }
        for (int j = 0; j < lab.colunas; j++) {
            if (lab.labirinto[0][j] == 'E') return new Coordenada(0, j);
            if (lab.labirinto[lab.linhas - 1][j] == 'E') return new Coordenada(lab.linhas - 1, j);
        }
        return null;
    }

    static void adicionarPossibilidades(Labirinto lab, Coordenada atual, Fila<Coordenada> fila) throws Exception {
        int l = atual.getLinha(), c = atual.getColuna();
        if (posValida(l - 1, c, lab)) fila.enfileira(new Coordenada(l - 1, c));
        if (posValida(l + 1, c, lab)) fila.enfileira(new Coordenada(l + 1, c));
        if (posValida(l, c - 1, lab)) fila.enfileira(new Coordenada(l, c - 1));
        if (posValida(l, c + 1, lab)) fila.enfileira(new Coordenada(l, c + 1));
    }

    static boolean posValida(int l, int c, Labirinto lab) {
        return l >= 0 && l < lab.linhas && c >= 0 && c < lab.colunas &&
               (lab.labirinto[l][c] == ' ' || lab.labirinto[l][c] == 'S');
    }
}

class Labirinto {
    char[][] labirinto;
    int linhas, colunas;

    public Labirinto(String arquivo) throws IOException {
        Scanner sc = new Scanner(new File(arquivo));
        try {
            if (!sc.hasNextInt()) throw new IllegalArgumentException("Número de linhas inválido.");
            linhas = sc.nextInt();
            if (!sc.hasNextInt()) throw new IllegalArgumentException("Número de colunas inválido.");
            colunas = sc.nextInt();
            sc.nextLine();

            labirinto = new char[linhas][colunas];
            for (int i = 0; i < linhas; i++) {
                if (!sc.hasNextLine()) throw new IllegalArgumentException("Linhas insuficientes no arquivo.");
                String linha = sc.nextLine();
                if (linha.length() != colunas) throw new IllegalArgumentException("Linha " + (i + 1) + " com tamanho inválido.");
                for (int j = 0; j < colunas; j++) {
                    char ch = linha.charAt(j);
                    if (ch != ' ' && ch != 'E' && ch != 'S' && ch != '*') {
                        throw new IllegalArgumentException("Caractere inválido '" + ch + "' na linha " + (i + 1) + ".");
                    }
                    labirinto[i][j] = ch;
                }
            }
        } finally {
            sc.close();
        }
    }

    void mostrar() {
        for (char[] l : labirinto) {
            System.out.println(new String(l));
        }
        System.out.println();
    }
}