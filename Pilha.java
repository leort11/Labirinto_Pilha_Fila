
public class Pilha<T> {
    private final Object[] elementos;
    private int topo = 0;

    public Pilha(int capacidade) throws Exception {
        if (capacidade <= 0)
            throw new Exception("Capacidade inválida");
        elementos = new Object[capacidade];
    }

    public void push(T item) throws Exception {
        if (topo == elementos.length)
            throw new Exception("Pilha cheia");
        elementos[topo++] = item;
    }

    @SuppressWarnings("unchecked")
    public T pop() throws Exception {
        if (topo == 0)
            throw new Exception("Pilha vazia");
        T ret = (T) elementos[--topo];
        elementos[topo] = null;
        return ret;
    }

    @SuppressWarnings("unchecked")
    public T peek() throws Exception {
        if (topo == 0)
            throw new Exception("Pilha vazia");
        return (T) elementos[topo - 1];
    }

    public boolean isEmpty() {
        return topo == 0;
    }

    public int size() {
        return topo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < topo; i++) {
            sb.append(elementos[i]);
            if (i < topo - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}
