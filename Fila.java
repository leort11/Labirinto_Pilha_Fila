
public class Fila<T> {
    private final Object[] buffer;
    private int head = 0, tail = 0, size = 0;

    // Construtor para inicializar a fila com uma capacidade específica
    public Fila(int capacidade) throws Exception {
        if (capacidade <= 0)
            throw new Exception("Capacidade inválida");
        buffer = new Object[capacidade];
    }

    // Adiciona um item à fila
    public void enfileira(T item) throws Exception {
        if (size == buffer.length)
            throw new Exception("Fila cheia");
        buffer[tail] = item;
        tail = (tail + 1) % buffer.length;
        size++;
    }

    // Remove e retorna o item na frente da fila
    @SuppressWarnings("unchecked")
    public T remove() throws Exception {
        if (size == 0)
            throw new Exception("Fila vazia");
        T ret = (T) buffer[head];
        buffer[head] = null;
        head = (head + 1) % buffer.length;
        size--;
        return ret;
    }

    // Retorna o item na frente da fila sem removê-lo
    @SuppressWarnings("unchecked")
    public T peek() throws Exception {
        if (size == 0)
            throw new Exception("Fila vazia");
        return (T) buffer[head];
    }

    // Retorna o número de elementos na fila
    public int size() {
        return size;
    }

    // Verifica se a fila está vazia
    public boolean isEmpty() {
        return size == 0;
    }

    // Limpa todos os elementos da fila
    public void clear() {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

    // Representação em String da fila (para depuração)
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Fila: [");
        for (int i = 0; i < size; i++) {
            sb.append(buffer[(head + i) % buffer.length]);
            if (i < size - 1) sb.append(", ");
        }
        sb.append("]");
        return sb.toString();
    }
}