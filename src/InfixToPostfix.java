import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.regex.Matcher;
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
                next = scan.next(); // this only works for single digit numbers, I need to place space after every number
                stringBuilder.append(next);
            } else if (scan.hasNext(OPERATOR)) {
                next = scan.next();
                stack.push(next.charAt(0));
            } else if (scan.hasNext("\\)")) { // todo: This block needs work.
                scan.next();
                Character temp;

                try {
                    temp = stack.peek();
                    if (temp.toString().matches("[+\\-*/]")) {
                        stringBuilder.append(stack.pop());
                    } else throw new EmptyStackException();
                } catch (EmptyStackException e) {
                    System.err.println("Too few operations in the infix expression.");
                    System.exit(1);
                }
                try {
                    temp = stack.peek();
                    if (temp == '(') {
                        stack.pop();
                    }
                } catch (EmptyStackException e) {
                    System.err.println("No balanced parentheses.");
                    System.exit(1);
                }
            }

        } while (scan.hasNext());

        if (!stack.isEmpty()) {
            System.err.println("Expression wasn't fully parenthesized.");
        }

        return stringBuilder.toString();
    }



    public static void main(String[] args) {
        System.out.println(infixToPostfix("(1+2)"));
    }
}
