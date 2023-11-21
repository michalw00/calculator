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
                while (!stack.isEmpty() && stack.peek() != '(' && firstPrecedes(next.charAt(0), stack.peek())) {
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
                    System.err.println("Unbalanced parenthesis. 1");
                    System.exit(1);
                }
            }
        } while (scan.hasNext());

            while (!stack.isEmpty()) {
                if (stack.peek() == '(') {
                    System.err.println("Unbalanced parenthesis. 2");
                    System.exit(1);
                }
                stringBuilder.append(stack.pop());
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



    public static void main(String[] args) {
        System.out.println(infixToPostfix("(12+2)*2"));
    }
}
