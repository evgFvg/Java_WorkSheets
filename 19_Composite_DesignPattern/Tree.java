import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Tree implements Component {
    public static void main(String[] args) {
        Tree t = new Tree("/home/jey/Downloads/tree");
        t.print();
    }

    private final Component component;

    Tree(String s) {
        File file = new File(s);
        if (!file.exists()) {
            throw new NullPointerException("Such a file doesn't exist");
        }
        this.component = file.isFile() ? new Leaf(s) : new Folder(file, 1);
    }

    @Override
    public void print() {
        component.print();
    }
}

class Leaf implements Component {
    private final String name;
    // ANSI escape code for yellow text
    public static final String ANSI_YELLOW = "\u001B[33m";
    // ANSI escape code for reset to default color
    private static final String ANSI_RESET = "\u001B[0m";

    Leaf(String name) {
        this.name = name;
    }

    @Override
    public void print() {
        System.out.println(ANSI_YELLOW + name + ANSI_RESET);
    }
}

class Folder implements Component {
    private final List<Component> listOfComponents = new ArrayList<>();
    private final int level;
    private final File file;

    public Folder(File file, int level) {
        this.level = level;
        this.file = file;
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isFile()) {
                listOfComponents.add(new Leaf(f.getName()));
            } else {
                listOfComponents.add(new Folder(f, level + 1));
            }
        }

        listOfComponents.sort(Component::compareTo);
    }


    @Override
    public void print() {
        System.out.println(file.getName());
        for (Component c : listOfComponents) {
            for (int i = 0; i < level; ++i) {
                System.out.print("\t");
            }
            c.print();
        }
    }

}
