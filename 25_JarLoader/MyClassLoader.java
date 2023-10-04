import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

public class MyClassLoader {
    private static final String classFilePostfix = ".class";
    private final String jarPath;
    private final String interfaceName;

    public MyClassLoader(String jarPath, String interfaceName) {
        this.jarPath = jarPath;
        this.interfaceName = interfaceName;
    }

    public  Class<?>[] load() throws IOException {
        List<Class<?>> resList = new ArrayList<>();
        final URL url = new File(jarPath).toURI().toURL();
        try(URLClassLoader urlClassLoader = new URLClassLoader(new URL[] {url})) {
            JarInputStream jis = new JarInputStream(new FileInputStream(jarPath));
            JarEntry jarEntry = null;
            final Class<?> iFace = urlClassLoader.loadClass(interfaceName);
            while((jarEntry = jis.getNextJarEntry()) != null) {
                if(!jarEntry.isDirectory() && jarEntry.getName().endsWith(classFilePostfix)) {
                    String className = jarEntry
                            .getName()
                            .replaceAll("/", "\\.")
                            .substring(0, jarEntry.getName().length() - 6);
                    final Class<?> loadedClass = urlClassLoader.loadClass(className);
                    if(iFace.isAssignableFrom(loadedClass) && !loadedClass.isInterface()) {
                        resList.add(loadedClass);
                    }
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return resList.toArray(new Class[0]);
    }
}


