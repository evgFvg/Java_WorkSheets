import java.io.IOException;

public interface IPeer {
    public byte[] read(int bytesToRead) throws IOException;

    public void write(byte[] data) throws IOException;

    public void close() throws IOException;
}

