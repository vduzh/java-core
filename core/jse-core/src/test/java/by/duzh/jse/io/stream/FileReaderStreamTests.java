package by.duzh.jse.io.stream;

import org.junit.Assert;
import org.junit.Test;

import java.io.*;

public class FileReaderStreamTests {
    private static final String JAVA_HOME_DIR = System.getProperty("java.home");
    private static final String FILE_NAME = "LICENSE";

    private final File file = new File(JAVA_HOME_DIR, FILE_NAME);

    @Test(expected = FileNotFoundException.class)
    public void testCreate() throws FileNotFoundException {
        // With path
        String path = JAVA_HOME_DIR + System.getProperty("file.separator") + FILE_NAME;
        try (Reader is = new FileReader(path)) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // With file
        try (Reader is = new FileReader(file)) {
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // With file
        try (Reader is = new FileReader(path + "foo")) {
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testMarkSupported() {
        try (Reader is = new FileReader(file)) {
            Assert.assertFalse(is.markSupported());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test(expected = IOException.class)
    public void testReset() throws IOException {
        try (Reader is = new FileReader(file)) {
            is.reset();
        }
    }
}
