package Business;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class GameObjectTest {
    @Test
    void testFromChar() {
        for (char c : new char[]{'W', ' ', 'C', 'D', 'S', 'O', '=', 'w', 'c', 'd', 's', 'o'}) {
            assertEquals(GameObject.fromChar(c).getCharSymbol(), Character.toUpperCase(c));
        }

        for (char c : new char[]{'a', 'q', 'P', 'G', '\\', '!', '(', '~', '>', '_'}) {
            assertEquals(GameObject.fromChar(c).getCharSymbol(), 'W');
        }
    }
}