import java.util.*;
import java.io.*;

class Coordenada {
    int linha, coluna;

    Coordenada(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public String toString() {
        return "(" + linha + "," + coluna + ")";
    }
}

class Labirinto {
    char[][] labirinto;
    int linhas, colunas;

    public Labirinto(String arquivo) throws IOException {
        Scanner sc = new Scanner(new File(arquivo));
        linhas = sc.nextInt();
        colunas = sc.nextInt();
        sc.nextLine();
        labirinto = new char[linhas][colunas];
        for (int i = 0; i < linhas; i++) {
            String linha = sc.nextLine();
            for (int j = 0; j < colunas; j++) {
                labirinto[i][j] = linha.charAt(j);
            }
        }
        sc.close();
    }

    void mostrar() {
        for (char[] linha : labirinto) {
            System.out.println(new String(linha));
        }
        System.out.println();
    }
}

public class ResolucaoLabirinto {
    public static void main(String[] args) throws IOException {
        Scanner input = new Scanner(System.in);
        System.out.print("Digite o nome do arquivo: ");
        String nomeArquivo = input.nextLine();

        Labirinto lab = new Labirinto(nomeArquivo);
        Stack<Coordenada> caminho = new Stack<>();
        Stack<Queue<Coordenada>> possibilidades = new Stack<>();
        
        Coordenada entrada = encontrarEntrada(lab);
        if (entrada == null) {
            System.out.println("Erro: Entrada não encontrada nas bordas.");
            return;
        }

        Coordenada atual = entrada;
        boolean achouSaida = false;

        while (true) {
            Queue<Coordenada> fila = new LinkedList<>();
            adicionarPossibilidades(lab, atual, fila);

            if (fila.isEmpty()) {
                if (caminho.isEmpty()) {
                    System.out.println("Sem caminho possível.");
                    break;
                }
                atual = caminho.pop();
                lab.labirinto[atual.linha][atual.coluna] = ' ';
                possibilidades.pop();
            } else {
                atual = fila.poll();
                if (lab.labirinto[atual.linha][atual.coluna] == 'S') {
                    achouSaida = true;
                    break;
                }
                lab.labirinto[atual.linha][atual.coluna] = '*';
                caminho.push(atual);
                possibilidades.push(fila);
            }
        }

        if (achouSaida) {
            lab.mostrar();
            Stack<Coordenada> inverso = new Stack<>();
            while (!caminho.isEmpty()) inverso.push(caminho.pop());

            System.out.println("Caminho da entrada até a saída:");
            while (!inverso.isEmpty()) {
                System.out.print(inverso.pop() + " ");
            }
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

    static void adicionarPossibilidades(Labirinto lab, Coordenada atual, Queue<Coordenada> fila) {
        int l = atual.linha, c = atual.coluna;
        if (posValida(l-1, c, lab)) fila.add(new Coordenada(l-1, c));
        if (posValida(l+1, c, lab)) fila.add(new Coordenada(l+1, c));
        if (posValida(l, c-1, lab)) fila.add(new Coordenada(l, c-1));
        if (posValida(l, c+1, lab)) fila.add(new Coordenada(l, c+1));
    }

    static boolean posValida(int l, int c, Labirinto lab) {
        return l >= 0 && l < lab.linhas && c >= 0 && c < lab.colunas &&
            (lab.labirinto[l][c] == ' ' || lab.labirinto[l][c] == 'S');
    }
}
