public interface Component {
    void print();
    default int compareTo(Component other) {
        boolean c1IsFolder = this instanceof Folder;
        boolean c2IsFolder = other instanceof Folder;

        if (c1IsFolder && !c2IsFolder) {
            return 1;
        } else if (!c1IsFolder && c2IsFolder) {
            return -1;
        } else {
            return 0;
        }
    }
}
