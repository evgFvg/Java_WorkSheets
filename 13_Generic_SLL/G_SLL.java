import java.time.LocalTime;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class G_SLL<E> implements Iterable<E> {
    private Node head = null;
    private volatile LocalTime sllModCount = null;

    public G_SLL() {
        this.sllModCount = LocalTime.now();
    }

    private class Node {
        private Node next = null;
        private E data = null;

        private Node(E data) {
            this.data = data;
        }
    }

    public class IteratorImp implements Iterator<E> {
        private Node node = null;
        private volatile LocalTime modCount = null;

        private IteratorImp(Node node) {
            this.node = node;
            this.modCount = getSllModCount();
        }

        @Override
        public boolean hasNext() {
            isModifiedSLL();
            return node != null;
        }

        @Override
        public E next() {
            isModifiedSLL();
            if (!hasNext()) {
                throw new NoSuchElementException("Illegal use of Iterator");
            }
            E res = node.data;
            node = node.next;
            return res;
        }

        private void isModifiedSLL() {
            if (!this.modCount.equals(getSllModCount())) {
                throw new ConcurrentModificationException("Illegal modification of G_SLL");
            }
        }
    }


    private void setModCount() {
        this.sllModCount = LocalTime.now();
    }

    private LocalTime getSllModCount() {
        return this.sllModCount;
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImp(this.head);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E elm : this) {
            sb.append("[ ").append(elm.toString()).append("]\n");
        }
        return sb.toString();
    }


    public boolean isEmpty() {
        return null == this.head;
    }

    public long getSize() {
        long size = 0;
        for (E e : this) {
            ++size;
        }
        return size;
    }

    public void pushFront(E data) {
        Node newNode = new Node(data);
        newNode.next = head;
        this.head = newNode;
        setModCount();
    }

    public E popFront() {
        E res = head.data;
        head = head.next;
        setModCount();
        return res;
    }


    public Iterator<E> find(Object obj) {
        Iterator<E> it = this.iterator();
        for (E elm : this) {
            if (elm.equals(obj)) {
                return it;
            } else {
                it.next();
            }
        }
        return it;
    }

    public void printSLL() {
        System.out.println(this);
    }

    public static <T> G_SLL<T> newReverse(G_SLL<T> list) {
        G_SLL<T> newList = new G_SLL<>();
        for (T elm : list) {
            newList.pushFront(elm);
        }
        return newList;
    }

    public static <E> G_SLL<E> merge(G_SLL<E> one, G_SLL<E> other) {
        G_SLL<E>.Node node = one.head;
        while (null != node.next) {
            node = node.next;
        }
        node.next = other.head;
        other.head = null;
        return one;
    }
}

