package service;

/**
 * The interface needed to get the result of equation string
 *
 * @see #calculate(String equation)
 */
public interface MathParserService {
    /**
     * Calculates the number of numbers in an equation
     *
     * @param equation to be tested
     *
     * @return number of numbers in an equation
     * 0 if equation == null or equation has incorrect characters
     *
     * @see MathParser#numberOfNumbers(String)
     */
    int numberOfNumbers(String equation);

    /**
     * Checks the equation for the absence of incorrect characters and incorrect combinations of characters
     *
     * @param equation to be tested
     *
     * @return true if equation can be calculated by {@link #calculate(String)}
     *
     * @see MathParser#equationIsCorrect
     */
    boolean equationIsCorrect(String equation);

    /**
     * Calculate result of equation
     * Can identify symbols '+', '-', '*', '/'
     * Can identify integers and doubles (doubles with dots and commas)
     * Can identify nesting levels created by symbols '(', ')'
     *
     * @param equation to calculate
     *
     * @return String with result in double
     * null if equation == null or if equation has incorrect characters
     *
     * Can be transformed to double
     *
     * @see MathParser#calculate(String)
     * @see Double#parseDouble(String)
     * @see #equationIsCorrect(String)
     * @see MathParser#equationIsCorrect(String)
     */
    String calculate(String equation);
}
