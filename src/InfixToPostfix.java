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

        do {

            if (scan.hasNext("\\(")) {
                next = scan.next();
                stack.push(next.charAt(0));
            } else if (scan.hasNext(UNSIGNED_DOUBLE)) {
                next = scan.next();
                stringBuilder.append(next).append(" ");
            } else if (scan.hasNext(OPERATOR)) {
                next = scan.next();
                while (!stack.isEmpty() && stack.peek() != '(' && firstPrecedes(stack.peek(), next.charAt(0))) {
                    stringBuilder.append(stack.pop()).append(" ");
                }
                stack.push(next.charAt(0));
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
        } while (scan.hasNext());

            while (!stack.isEmpty()) {
                if (stack.peek() == '(') {
                    System.err.println("Unbalanced parenthesis.");
                    System.exit(1);
                }
                stringBuilder.append(stack.pop()).append(" ");
            }


        return stringBuilder.toString().trim();
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
            if (input.hasNext(UNSIGNED_DOUBLE)) {
                next = input.findInLine(UNSIGNED_DOUBLE); // NOTE: finds next occurrence, that's how it scans the string, character by character
                stack.push(Double.valueOf(next));
            } else if (input.hasNext(CHARACTER)){
                next = input.findInLine(CHARACTER);

                if (next.matches("\\s+")) continue; // Skips space characters

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




    public static void main(String[] args) {
        System.out.println("evaluatePostfix result: "+evaluatePostfix(infixToPostfix("2+2*2")));
    }
}
