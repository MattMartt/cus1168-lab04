package academy.javapro;

class ExpressionParser {
    private final String input;
    private int position;

    public ExpressionParser(String input) {
        this.input = input;
        this.position = 0;
    }

    // expr → expr + term
    public double parseExpression() {
        double result = parseTerm();
        while (position < input.length()) {
            char operator = input.charAt(position);
            if (operator == '+') {
                position++;
                result += parseTerm();
            } else if (operator == '*' || operator == ')') {
                break;
            } else if (!Character.isWhitespace(operator)) {
                throw new IllegalArgumentException("Unexpected character: " + operator + " at position " + position);
            } else {
                position++;
            }
        }
        return result;
    }

    // term → term * factor
    private double parseTerm() {
        double result = parseFactor();
        while (position < input.length()) {
            char operator = input.charAt(position);
            if (operator == '*') {
                position++;
                result *= parseFactor();
            } else if (operator == '+' || operator == ')') {
                break;
            } else if (!Character.isWhitespace(operator)) {
                throw new IllegalArgumentException("Unexpected character: " + operator + " at position " + position);
            } else {
                position++;
            }
        }
        return result;
    }

    // factor → ( expr )
    private double parseFactor() {
        if (position < input.length() && input.charAt(position) == '(') {
            position++;
            double result = parseExpression();
            if (position >= input.length() || input.charAt(position) != ')') {
                throw new IllegalArgumentException("Expected closing parenthesis at position " + position);
            }
            position++;
            return result;
        } else if (position < input.length()) {
            return parseNumber();
        } else {
            throw new IllegalArgumentException("Unexpected end of input at position " + position);
        }
    }

    // Parse a numeric value
    private double parseNumber() {
        StringBuilder digits = new StringBuilder();
        while (position < input.length() && (Character.isDigit(input.charAt(position)) || input.charAt(position) == '.')) {
            digits.append(input.charAt(position));
            position++;
        }
        if (digits.length() == 0) {
            throw new IllegalArgumentException("Expected number at position " + position);
        }
        return Double.parseDouble(digits.toString());
    }

    public static void main(String[] args) {
        // Test cases
        String[] testCases = {
                "2 + 3 * (4 + 5)",    // Complex expression with parentheses
                "2 + 3 * 4",          // Basic arithmetic with precedence
                "(2 + 3) * 4",        // Parentheses changing precedence
                "2 * (3 + 4) * (5 + 6)", // Multiple parentheses
                "1.5 + 2.5 * 3"       // Decimal numbers
        };

        // Process each test case
        for (String expression : testCases) {
            System.out.println("\nTest Case: " + expression);
            try {
                ExpressionParser parser = new ExpressionParser(expression.replaceAll("\\s+", "")); // Remove spaces
                double result = parser.parseExpression();
                System.out.println("Result: " + result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}