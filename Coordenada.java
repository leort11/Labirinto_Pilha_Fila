
import java.util.Objects;

public class Coordenada {
    private int linha, coluna;

    public Coordenada(int linha, int coluna) {
        this.linha = linha;
        this.coluna = coluna;
    }

    public int getLinha() {
        return linha;
    }

    public void setLinha(int linha) {
        if (linha < 0) {
            throw new IllegalArgumentException("A linha não pode ser negativa.");
        }
        this.linha = linha;
    }

    public int getColuna() {
        return coluna;
    }

    public void setColuna(int coluna) {
        if (coluna < 0) {
            throw new IllegalArgumentException("A coluna não pode ser negativa.");
        }
        this.coluna = coluna;
    }

    @Override
    public String toString() {
        return "(" + linha + "," + coluna + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordenada that = (Coordenada) o;
        return linha == that.linha && coluna == that.coluna;
    }

    @Override
    public int hashCode() {
        return Objects.hash(linha, coluna);
    }
}