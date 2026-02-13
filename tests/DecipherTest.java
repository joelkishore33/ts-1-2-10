import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

public class DecipherTest {

    private static void writeKeyFile(Path path, String line1, String line2) throws IOException {
        Files.createDirectories(path.getParent());
        Files.writeString(path, line1 + System.lineSeparator() + line2);
    }

    @Test
    void decrypt_basicMapping() throws Exception {
        // key1 = original, key2 = encrypted
        Path keyPath = Paths.get("build", "test-keys", "key.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Decipher.getAltKey(keyPath.toString());

        assertEquals("abc", Decipher.decrypt("xyz")); // x->a, y->b, z->c
    }

    @Test
    void decrypt_keepsCharsNotInKey() throws Exception {
        Path keyPath = Paths.get("build", "test-keys", "key2.txt");
        writeKeyFile(keyPath, "abc", "xyz");

        Decipher.getAltKey(keyPath.toString());

        assertEquals("a! b", Decipher.decrypt("x! y")); // ! and space unchanged
    }

    @Test
    void decrypt_throwsIfKeyMissingLines() {
        assertThrows(IllegalArgumentException.class, () -> Decipher.decrypt("xyz"));
    }

    @Test
    void decrypt_throwsIfKeyLengthsMismatch() throws Exception {
        Path keyPath = Paths.get("build", "test-keys", "badkey.txt");
        writeKeyFile(keyPath, "abcd", "xyz"); // mismatched lengths

        Decipher.getAltKey(keyPath.toString());
        assertThrows(IllegalArgumentException.class, () -> Decipher.decrypt("x"));
    }
}