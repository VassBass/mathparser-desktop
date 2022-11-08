package service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MathParserTest {
    private static final MathParserService mathParser = new MathParser();

    /**
     * @see MathParserService#numberOfNumbers(String)
     */
    @Test
    void numberOfNumbers() {
        assertEquals(7, mathParser.numberOfNumbers("(12,01*5478 - (-2*3) -3.21 - 6/2)"));
        assertEquals(0, mathParser.numberOfNumbers(null));
        assertEquals(0, mathParser.numberOfNumbers(" +--+ "));
    }

    /**
     * @see MathParserService#equationIsCorrect(String)
     */
    @Test
    void equationIsCorrect() {
        assertTrue(mathParser.equationIsCorrect("(12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertTrue(mathParser.equationIsCorrect("-(12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertTrue(mathParser.equationIsCorrect("+12,01*5478 - (-2*3) -3.21 - 6/-2"));
        assertTrue(mathParser.equationIsCorrect("(+12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertTrue(mathParser.equationIsCorrect("(12,01*5478 -+ (-2*3) -3.21 - 6/-2)"));
        assertTrue(mathParser.equationIsCorrect("(++++++++12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertTrue(mathParser.equationIsCorrect("(12,01*5478 - (-2*3)( -3.21 - 6)/-2)"));
        assertTrue(mathParser.equationIsCorrect("(12,01*5478 - 2*3( -3.21 - 6)/-2)"));

        assertFalse(mathParser.equationIsCorrect(" +--+ "));
        assertFalse(mathParser.equationIsCorrect(null));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*3-) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*/3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*/3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478) - (-2*3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("((12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12(,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,)01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,.01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2.*/3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*3) -3.2.1 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*3) -3.21 - .6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*3) -3Y.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 -- (2*3) -3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*3) ---3.21 - 6/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - (-2*3) -3.21 - 6/0)"));
        assertFalse(mathParser.equationIsCorrect("(12,01*5478 - 2*3.( -3.21 - 6)/-2)"));
        assertFalse(mathParser.equationIsCorrect("(12,*5478 - (-2*3) -3.21 - 6/-2)"));
    }

    /**
     * @see MathParserService#calculate(String)
     */
    @Test
    void calculate() {
        assertEquals("65796.56999999999",mathParser.calculate("(12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertEquals("-65796.56999999999",mathParser.calculate("-(12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertEquals("65796.56999999999", mathParser.calculate("+12,01*5478 -+ (-2*3) -3.21 - 6/-2"));
        assertEquals("65796.56999999999",mathParser.calculate("(+12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertEquals("65796.56999999999",mathParser.calculate("(12,01*5478 -+ (-2*3) -3.21 - 6/-2)"));
        assertEquals("65796.56999999999",mathParser.calculate("(++++++++12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertEquals("65818.41",mathParser.calculate("(12,01*5478 - (-2*3)( -3.21 - 6)/-2)"));
        assertEquals("65763.15",mathParser.calculate("(12,01*5478 - 2*3( -3.21 - 6)/-2)"));
        assertEquals("22.0", mathParser.calculate("7+5*3"));
        assertEquals("6.0", mathParser.calculate("2+2*2"));

        assertNull(mathParser.calculate(" +--+ "));
        assertNull(mathParser.calculate(null));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*3-) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*/3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*/3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478) - (-2*3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("((12,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12(,01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,)01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,.01*5478 - (-2*3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2.*/3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*3) -3.2.1 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*3) -3.21 - .6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*3) -3Y.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 -- (2*3) -3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*3) ---3.21 - 6/-2)"));
        assertNull(mathParser.calculate("(12,01*5478 - (-2*3) -3.21 - 6/0)"));
        assertNull(mathParser.calculate("(12,01*5478 - 2*3.( -3.21 - 6)/-2)"));
        assertNull(mathParser.calculate("(12,*5478 - (-2*3) -3.21 - 6/-2)"));
    }
}