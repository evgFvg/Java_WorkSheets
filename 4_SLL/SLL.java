
public class SLL {
    private Node head = null;
    private long size = 0;

    private class Node {
        private Node next;
        private Object data;

        private Node(Object data) {
            this.next = null;
            this.data = data;
        }
    }

    private class IterImp implements ListIterator {
        private Node node;

        private IterImp(Node node) {
            this.node = node;
        }

        @Override
        public boolean hasNext() {
            return node != null;
        }

        @Override
        public Object next() {
            if (!hasNext()) {
                return null;
            }
            Object res = node.data;
            node = node.next;
            return res;
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public long getSize() {
        return size;
    }

    public void pushFront(Object data) {
        Node newNode = new Node(data);
        newNode.next = head;
        head = newNode;
        size++;
    }

    public Object popFront() {
        if (isEmpty() == true) {
            head = null;
            return null;
        }
        Object res = head.data;
        head = head.next;
        --size;
        return res;
    }

    public ListIterator begin() {
        return head == null ? null : new IterImp(head);
    }

    public ListIterator find(Object obj) {
        Node cur = head;
        while (cur != null && false == cur.data.equals(obj)) {
            cur = cur.next;
        }
        return cur == null ? null : new IterImp(cur);
    }
}
