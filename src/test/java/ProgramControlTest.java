import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProgramControlTest {
    @Mock
    private FileHandler fileHandler;
    @Mock
    private Cipher cipher;

    private ProgramControl programControl;

    @BeforeEach
    void setUp() {
        programControl = new ProgramControl(fileHandler, cipher);
    }

    @Test
    void testFetchFile_NoArgs() {
        String[] fileList = {"01 filea.txt", "02 fileb.txt", "03 filec.txt"};
        when((String[]) fileHandler.readFile()).thenReturn(fileList);
        assertArrayEquals(fileList, programControl.fetchFile());

        verify(fileHandler).readFile();
    }

    @Test
    void testFetchFile_OneArg() throws FileNotFoundException {
        String fileName = "test.txt";

        when(fileHandler.readFile(fileName)).thenReturn("Ciphered File Context");
        when(cipher.decipher("Ciphered File Context")).thenReturn("Deciphered File Context");
        assertEquals("Deciphered File Context", programControl.fetchFile(fileName));

        verify(fileHandler).readFile(fileName);
    }

    @Test
    void testFetchFile_OneArg_FileNotFound() throws FileNotFoundException {
        when(fileHandler.readFile(null)).thenThrow(new FileNotFoundException());
        assertThrows(FileNotFoundException.class, () -> programControl.fetchFile(null));

        verify(fileHandler).readFile(null);
    }

    @Test
    void testFetchFile_OneArg_IllegalArgument() throws FileNotFoundException {
        String fileName = "test.txt";
        when(fileHandler.readFile(null)).thenReturn("Ciphered File Context");
        when(cipher.decipher("Ciphered File Context")).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> programControl.fetchFile(fileName));

        verify(fileHandler).readFile(null);
    }

    @Test
    void testFetchFile_TwoArgs_FileNotFound() throws FileNotFoundException {
        when(fileHandler.readFile(null)).thenThrow(new FileNotFoundException());
        assertThrows(RuntimeException.class, () -> programControl.fetchFile(null));

        verify(fileHandler).readFile(null);
    }

    @Test
    void testFetchFile_TwoArgs_IllegalArgument() throws FileNotFoundException {
        String fileName = "test.txt";
        when(fileHandler.readFile(fileName)).thenReturn("Ciphered File Context");
        when(cipher.decipher("Ciphered File Context")).thenThrow(new IllegalArgumentException());
        assertThrows(IllegalArgumentException.class, () -> programControl.fetchFile(null));

        verify(fileHandler).readFile(null);
    }

    @Test
    void testFetchFile_BothArgs() throws FileNotFoundException {
        String fileName = "test.txt";
        String key = "bcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890a";
        when(fileHandler.readFile(fileName)).thenReturn("Ciphered File Content");
        when(cipher.decipher("Ciphered File Content", key)).thenReturn("Deciphered File Content");
        assertEquals("Deciphered File Content", programControl.fetchFile(fileName));

        verify(fileHandler).readFile(fileName);
    }
}