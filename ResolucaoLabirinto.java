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

            Labirinto lab = new Labirinto(nomeArquivo);
            Pilha<Coordenada> caminho = new Pilha<>(lab.linhas * lab.colunas);
            Pilha<Fila<Coordenada>> possibilidades = new Pilha<>(lab.linhas * lab.colunas);
            boolean[][] visitado = new boolean[lab.linhas][lab.colunas];

            Coordenada atual = encontrarEntrada(lab);
            visitado[atual.getLinha()][atual.getColuna()] = true;

            boolean achouSaida = false;

            while (true) {
                Fila<Coordenada> fila = new Fila<>(4);
                adicionarPossibilidades(lab, atual, fila, visitado);

                if (fila.isEmpty()) {
                    if (caminho.isEmpty() && possibilidades.isEmpty()) {
                        System.out.println("Não existe caminho até a saída.");
                        break;
                    }
                    // modo regressivo: retrocede para último ponto e retoma fila
                    lab.labirinto[atual.getLinha()][atual.getColuna()] = ' ';
                    atual = caminho.pop();
                    fila = possibilidades.pop();
                } else {
                    // modo progressivo: salva fila e avança
                    possibilidades.push(fila);
                    atual = fila.remove();
                    caminho.push(atual);
                    visitado[atual.getLinha()][atual.getColuna()] = true;

                    if (lab.labirinto[atual.getLinha()][atual.getColuna()] == 'S') {
                        achouSaida = true;
                        break;
                    }
                    lab.labirinto[atual.getLinha()][atual.getColuna()] = '*';
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

    static void adicionarPossibilidades(Labirinto lab, Coordenada atual, Fila<Coordenada> fila, boolean[][] visitado) throws Exception {
        int l = atual.getLinha(), c = atual.getColuna();
        if (posValida(l - 1, c, lab) && !visitado[l - 1][c]) fila.enfileira(new Coordenada(l - 1, c));
        if (posValida(l + 1, c, lab) && !visitado[l + 1][c]) fila.enfileira(new Coordenada(l + 1, c));
        if (posValida(l, c - 1, lab) && !visitado[l][c - 1]) fila.enfileira(new Coordenada(l, c - 1));
        if (posValida(l, c + 1, lab) && !visitado[l][c + 1]) fila.enfileira(new Coordenada(l, c + 1));
    }

    static boolean posValida(int l, int c, Labirinto lab) {
        return l >= 0 && l < lab.linhas && c >= 0 && c < lab.colunas &&
               (lab.labirinto[l][c] == ' ' || lab.labirinto[l][c] == 'S');
    }
}

class Labirinto {
    char[][] labirinto;
    int linhas, colunas;

    public Labirinto(String arquivo) throws Exception {
        File f = new File(arquivo);
        if (!f.exists() || !f.isFile())
            throw new Exception("Arquivo não encontrado: " + arquivo);

        Scanner sc = new Scanner(f);
        if (!sc.hasNextInt()) throw new Exception("Falta número de linhas");
        linhas = sc.nextInt();
        if (!sc.hasNextInt()) throw new Exception("Falta número de colunas");
        colunas = sc.nextInt();
        sc.nextLine();

        if (linhas <= 0 || colunas <= 0)
            throw new Exception("Dimensões inválidas: " + linhas + "x" + colunas);

        labirinto = new char[linhas][colunas];
        int countE = 0, countS = 0;

        for (int i = 0; i < linhas; i++) {
            if (!sc.hasNextLine())
                throw new Exception("Número de linhas do labirinto menor que o esperado");

            String linha = sc.nextLine();
            if (linha.length() != colunas)
                throw new Exception("Linha " + i + " tem tamanho diferente de " + colunas);

            for (int j = 0; j < colunas; j++) {
                char c = linha.charAt(j);
                switch (c) {
                    case '#':
                    case ' ':
                        labirinto[i][j] = c;
                        break;
                    case 'E':
                        if (i != 0 && i != linhas - 1 && j != 0 && j != colunas - 1)
                            throw new Exception("Entrada 'E' não está na borda: (" + i + "," + j + ")");
                        countE++;
                        labirinto[i][j] = c;
                        break;
                    case 'S':
                        countS++;
                        labirinto[i][j] = c;
                        break;
                    default:
                        throw new Exception("Caracter inválido: '" + c + "' em (" + i + "," + j + ")");
                }
            }
        }
        sc.close();

        if (countE == 0) throw new Exception("Nenhuma entrada 'E' encontrada");
        if (countE > 1) throw new Exception("Mais de uma entrada 'E' encontrada");
        if (countS == 0) throw new Exception("Nenhuma saída 'S' encontrada");
        if (countS > 1) throw new Exception("Mais de uma saída 'S' encontrada");
    }

    void mostrar() {
        for (char[] linha : labirinto) {
            System.out.println(new String(linha));
        }
        System.out.println();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Labirinto labirinto1 = (Labirinto) o;
        if (linhas != labirinto1.linhas || colunas != labirinto1.colunas) return false;
        for (int i = 0; i < linhas; i++) {
            for (int j = 0; j < colunas; j++) {
                if (labirinto[i][j] != labirinto1.labirinto[i][j]) return false;
            }
        }
        return true;
    }
}
