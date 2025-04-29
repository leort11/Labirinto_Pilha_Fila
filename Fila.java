public class Fila<T> {
    private final Object[] buffer;
    private int head = 0, tail = 0, size = 0;

    public Fila(int capacidade) throws Exception {
        if (capacidade <= 0)
            throw new Exception("Capacidade inválida");
        buffer = new Object[capacidade];
    }

    public void enfileira(T item) throws Exception {
        if (size == buffer.length)
            throw new Exception("Fila cheia");
        buffer[tail] = item;
        tail = (tail + 1) % buffer.length;
        size++;
    }

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

    @SuppressWarnings("unchecked")
    public T peek() throws Exception {
        if (size == 0)
            throw new Exception("Fila vazia");
        return (T) buffer[head];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = null;
        }
        head = 0;
        tail = 0;
        size = 0;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fila<?> fila = (Fila<?>) o;
        if (size != fila.size) return false;
        for (int i = 0; i < size; i++) {
            if (!buffer[(head + i) % buffer.length].equals(fila.buffer[(fila.head + i) % fila.buffer.length]))
                return false;
        }
        return true;
    }
}