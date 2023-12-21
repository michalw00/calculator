import java.util.Scanner;
import java.util.regex.Pattern;

public class InfixToPostfix {
    public static final Pattern UNSIGNED_DOUBLE = Pattern.compile("(\\d+\\.?\\d*|\\.\\d+([Ee][-+]?\\d+)?)");
    public static final Pattern CHARACTER = Pattern.compile("\\S.*?");
    public static final Pattern OPERATOR = Pattern.compile("[+\\-*/]");

    public static String infixToPostfix(String expression) {
        CustomStack<Character> stack = new CustomStack<>();
        Scanner scan = new Scanner(expression).useDelimiter("((?<=\\+)|(?=\\+)|(?<=-)|(?=-)|(?<=\\*)|(?=\\*)|(?<=/)|(?=/)|(?<=\\()|(?=\\)))");
        StringBuilder stringBuilder = new StringBuilder();
        String next;

        boolean lastTokenWasBinaryOperator = true;

        do {

            if (scan.hasNext("\\(")) {
                next = scan.next();
                stack.push(next.charAt(0));
                lastTokenWasBinaryOperator = false;
            } else if (scan.hasNext(UNSIGNED_DOUBLE)) {
                next = scan.next();
                stringBuilder.append(next).append(" ");
                lastTokenWasBinaryOperator = false;
            } else if (scan.hasNext(OPERATOR)) {
                next = scan.next();
                if (lastTokenWasBinaryOperator && next.charAt(0) == '-') {
                    // This is a unary operator, not a binary operator.
                    stringBuilder.append(next);
                    next = scan.next();
                    stringBuilder.append(next).append(" ");
                    lastTokenWasBinaryOperator = false;
                } else {
                    while (!stack.isEmpty() && stack.peek() != '(' && firstPrecedes(stack.peek(), next.charAt(0))) {
                        stringBuilder.append(stack.pop()).append(" ");
                    }
                    stack.push(next.charAt(0));
                    lastTokenWasBinaryOperator = true;
                }
            } else {
                if (!scan.hasNext("\\)")) {
                    System.err.println("Something went wrong, no right parenthesis was present.");
                    System.exit(1);
                }
                scan.next();

                while (!stack.isEmpty() && stack.peek() != '(') {
                    stringBuilder.append(stack.pop()).append(" ");
                }
                if (!stack.isEmpty() && stack.peek() == '(') {
                    stack.pop();
                } else {
                    System.err.println("Unbalanced parenthesis.");
                    System.exit(1);
                }
            }
        } while (scan.hasNext()); // todo: add division by zero check, it needs to be done here because of graphing mode

            while (!stack.isEmpty()) {
                if (stack.peek() == '(') {
                    System.err.println("Unbalanced parenthesis.");
                    System.exit(1);
                }
                stringBuilder.append(stack.pop()).append(" ");
            }


        return stringBuilder.toString().trim();
    }

    public static String replaceVariableWithArgumentValue(String expression, Double argument) {
        String[] stringArray = new String[expression.length()];
        char[] charArray = expression.toCharArray();

        for (int i = 0; i < stringArray.length; i++)
            stringArray[i] = Character.toString(charArray[i]);

        String currentString;
        for (int i = 0; i < stringArray.length; i++) {
            if (stringArray[i].charAt(0) == 'x') {
                currentString = argument.toString();
                stringArray[i] = currentString;
            }
        }

        StringBuilder concatenatedString = new StringBuilder();
        for (String str : stringArray) {
            concatenatedString.append(str);
        }

        return concatenatedString.toString();
    }

    private static boolean firstPrecedes(char firstOperator, char secondOperator) {
        switch (firstOperator) {
            case '+':
                switch (secondOperator) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        return false;
                }
            case '-':
                switch (secondOperator) {
                    case '+':
                    case '-':
                    case '*':
                    case '/':
                        return false;
                }
            case '*':
                switch (secondOperator) {
                    case '+':
                    case '-':
                        return true;
                    case '*':
                    case '/':
                        return false;
                }
            case '/':
                switch (secondOperator) {
                    case '+':
                    case '-':
                        return true;
                    case '*':
                    case '/':
                        return false;
                }
            default:
                System.err.println("This shouldn't have happened.");
                System.exit(1);
        }
        return false;
    }

    public static double evaluatePostfix(String expression) {
        CustomStack<Double> stack = new CustomStack<>();
        Scanner input = new Scanner(expression);
        String next;

        do {
            if (input.hasNext("-" + UNSIGNED_DOUBLE)) {
                next = input.findInLine(UNSIGNED_DOUBLE);
                stack.push(Double.valueOf("-"+next));
            } else if (input.hasNext(UNSIGNED_DOUBLE)) {
                next = input.findInLine(UNSIGNED_DOUBLE);
                stack.push(Double.valueOf(next));
            } else if (input.hasNext(CHARACTER)){
                next = input.findInLine(CHARACTER);

                if (next.matches("\\s+")) continue; // Skips space characters.

                double temp2 = stack.pop();
                double temp1 = stack.pop();

                double result;
                switch(next.charAt(0)) {
                    case '*':
                        result = temp1 * temp2;
                        break;
                    case '/':
                        result = temp1 / temp2;
                        break;
                    case '+':
                        result = temp1 + temp2;
                        break;
                    case '-':
                        result = temp1 - temp2;
                        break;
                    default:
                        throw new IllegalArgumentException("Illegal character");
                }
                stack.push(result);
            }
        } while(input.hasNext());
        input.close();
        return stack.pop();

    }

}
