import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComplexNumberTest {

    @Test
    void getImaginary() {
        ComplexNumber cn = new ComplexNumber(50.0, 15);
        assertEquals(50.0, cn.getReal());
        assertEquals(15, cn.getImaginary());
    }

    @Test
    void add() {
        ComplexNumber cn1 = new ComplexNumber(50.0, 15.0);
        ComplexNumber cn2 = new ComplexNumber(35.0, 16.0);

        ComplexNumber newCn = cn1.add(cn2);
        assertEquals(50.0 + 35, newCn.getReal());
        assertEquals(15.0 + 16.0, newCn.getImaginary());
    }

    @Test
    void subtract() {
        ComplexNumber cn1 = new ComplexNumber(50.0, 19.0);
        ComplexNumber cn2 = new ComplexNumber(35.0, 16.0);

        ComplexNumber newCn = cn1.subtract(cn2);
        assertEquals(50.0 - 35.0, newCn.getReal());
        assertEquals(19.0 - 16.0, newCn.getImaginary());
    }

    @Test
    void parse() {
        ComplexNumber parsed = ComplexNumber.parse("3.5+5i");
        assertEquals(3.5, parsed.getReal());
        assertEquals(5, parsed.getImaginary());

        ComplexNumber parsed2 = ComplexNumber.parse("+30 - 36.85i");
        assertEquals(30, parsed2.getReal());
        assertEquals(-36.85, parsed2.getImaginary());

        ComplexNumber problem = ComplexNumber.parse("5i");
        assertNull(problem);

        ComplexNumber problem2 = ComplexNumber.parse("+30 - 36");
        assertNull(problem2);
    }

    @Test
    void testToString() {
        ComplexNumber cn1 = new ComplexNumber(50.0, 19.0);
        assertTrue("50.0+19.0i".equals(cn1.toString()));
        ComplexNumber cn2 = new ComplexNumber(50.0, -19.0);
        assertTrue("50.0-19.0i".equals(cn2.toString()));
    }

    @Test
    void testEquals() {
        ComplexNumber cn1 = new ComplexNumber(50.0, 19.0);
        ComplexNumber cn2 = new ComplexNumber(50.0, 19.0);
        ComplexNumber notEqual = new ComplexNumber(5.0, 4.0);

        assertTrue(cn1.equals(cn2));
        assertFalse(cn1.equals(notEqual));

    }

    @Test
    void compareTo() {
        ComplexNumber cn1 = new ComplexNumber(50.0, 19.0);
        ComplexNumber same = new ComplexNumber(50.0, 19.0);
        ComplexNumber bigger = new ComplexNumber(100, 25);
        ComplexNumber less = new ComplexNumber(2.0, 3.0);

        assertTrue( cn1.compareTo(same) == 0);
        assertTrue( bigger.compareTo(cn1) > 0);
        assertTrue( less.compareTo(cn1) < 0);
    }
}
