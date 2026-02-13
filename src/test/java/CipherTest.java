import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class CipherTest {

    private static void writeKeyFile(Path path, String line1, String line2) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, line1 + System.lineSeparator() + line2);
    }

    private static void writeOneLineKeyFile(Path path, String line1) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, line1 + System.lineSeparator());
    }

    private static void writeEmptyFile(Path path) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, "");
    }

    @Test
    void loadKey_usesDefaultPath() throws Exception {
        Cipher cipher = new Cipher();
        cipher.loadKey();
        assertNotNull(cipher.decipher(""));
    }

    @Test
    void getAltKey_correctMapping() throws Exception {
        Path keyPath = Paths.get("build", "testkey.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());

        assertEquals("abc", cipher.decipher("xyz"));
    }

    @Test
    void getAltKey_loadsAlternateKey() throws Exception {
        Path altPath = Paths.get("build", "altkey.txt");
        writeKeyFile(altPath, "123", "789");

        Cipher cipher = new Cipher();
        cipher.getAltKey(altPath.toString());

        assertEquals("123", cipher.decipher("789"));
    }

    @Test
    void decrypt_Success() throws Exception {
        Path keyPath = Paths.get("build", "testkey.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());

        String result = cipher.decipher("xyz");
        assertEquals("abc", result);
    }

    @Test
    void decipher_emptyInput_returnsEmpty() throws Exception {
        Path keyPath = Paths.get("build", "k_empty_input.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());

        assertEquals("", cipher.decipher(""));
    }

    @Test
    void decipher_charsNotInKey2_staySame() throws Exception {
        Path keyPath = Paths.get("build", "k_unknown_chars.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());
        assertEquals("a! b", cipher.decipher("x! y"));
    }

    @Test
    void decipher_mixedMappedAndUnmapped_chars() throws Exception {
        Path keyPath = Paths.get("build", "k_mixed.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());
        assertEquals("a!c", cipher.decipher("x!z"));
    }

    @Test
    void getAltKey_emptyFile_throwsFileNotFoundException() throws Exception {
        Path keyPath = Paths.get("build", "k_empty.txt");
        writeEmptyFile(keyPath);

        Cipher cipher = new Cipher();
        assertThrows(java.io.FileNotFoundException.class, () -> cipher.getAltKey(keyPath.toString()));
    }

    @Test
    void getAltKey_missingFile_throwsFileNotFoundException() {
        Cipher cipher = new Cipher();
        assertThrows(java.io.FileNotFoundException.class,
                () -> cipher.getAltKey("build/definitely_not_real_key_file_12345.txt"));
    }

    @Test
    void getAltKey_oneLineFile() throws Exception {
        Path keyPath = Paths.get("build", "k_one_line.txt");
        writeOneLineKeyFile(keyPath, "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());

        assertEquals("abc", cipher.decipher("xyz"));
    }

    @Test
    void getAltKey_oneLineFile_fallback_unmappedStaysSame() throws Exception {
        Path keyPath = Paths.get("build", "k_one_line_unmapped.txt");
        writeOneLineKeyFile(keyPath, "xyz");

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());

        assertEquals("ab!c", cipher.decipher("xy!z"));
    }

    @Test
    void decipher_keyLengthMismatch_throwsIllegalArgumentException() throws Exception {
        Path keyPath = Paths.get("build", "k_mismatch.txt");
        writeKeyFile(keyPath, "abcd", "xy"); // different lengths

        Cipher cipher = new Cipher();
        cipher.getAltKey(keyPath.toString());

        assertThrows(IllegalArgumentException.class, () -> cipher.decipher("x"));
    }

    @Test
    void decipher_withoutLoadingKeys_throwsSomeException() {
        Cipher cipher = new Cipher();
        assertThrows(RuntimeException.class, () -> cipher.decipher("abc"));
    }
}
