import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class infixToPostfix {
    public static final Pattern UNSIGNED_DOUBLE = Pattern.compile("((\\d+\\.?\\d*)|(\\.\\d+))([Ee][-+]?\\d+)?.*?");
    public static final Pattern CHARACTER = Pattern.compile("\\S.*?");

    public static String infixToPostfix(String expression) {
        CustomStack<Character> stack = new CustomStack<>();
        Scanner scan = new Scanner(expression);
        StringBuilder stringBuilder = new StringBuilder();
        String next;

        do {

            if (scan.hasNext("\\(")) {
                stack.push(scan.findInLine("\\(").charAt(0));
                scan.next();
            } else if (scan.hasNext(UNSIGNED_DOUBLE)) { // todo: this condition is false for some reason
                next = scan.findInLine(UNSIGNED_DOUBLE);
                stringBuilder.append(next.charAt(0));
                scan.next();
            } else if (scan.hasNext("[+\\-*/]")) {
                next = scan.findInLine("[+\\-*/]");
                stringBuilder.append(next.charAt(0));
                scan.next();
            } else if (scan.hasNext("\\)")) {
                scan.next();
                try {
                    stringBuilder.append(stack.pop());
                } catch (EmptyStackException e) {
                    System.err.println("Too few operations in the infix expression.");
                    System.exit(1);
                }
                if (stack.peek() == '(') {
                    stack.pop();
                } else {
                    System.err.println("No balanced parentheses.");
                    System.exit(1);
                }
            }

        } while (scan.hasNext()); // expression characters need to be separated by spaces for this to work

        if (!stack.isEmpty()) {
            System.err.println("Expression wasn't fully parenthesized.");
        }

        return stringBuilder.toString();
    }



    public static void main(String[] args) {
        System.out.println(infixToPostfix("( 1 + 2 )"));
    }
}
