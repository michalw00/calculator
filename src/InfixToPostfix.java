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
        Matcher matcherNumbers = UNSIGNED_DOUBLE.matcher(expression);
        Matcher matcherOperators = OPERATOR.matcher(expression);
        Scanner scan = new Scanner(expression).useDelimiter("((?<=\\+)|(?=\\+)|(?<=-)|(?=-)|(?<=\\*)|(?=\\*)|(?<=/)|(?=/)|(?<=\\()|(?=\\)))");
        StringBuilder stringBuilder = new StringBuilder();
        String next;

        do {

            if (scan.hasNext("\\(")) { // Note: Scanner for some reason didn't work with more complex patterns, I had to use Matcher for this reason.
                stack.push(scan.findInLine("\\(").charAt(0));
            } else if (matcherNumbers.find()) {
                next = matcherNumbers.group(); // this only works for single digit numbers, I need to place space after every number
                stringBuilder.append(next);
            } else if (matcherOperators.find()) {
                next = matcherOperators.group();
                stack.push(next.charAt(0));
            }
            if (scan.hasNext("\\)")) {
                try {
                    stringBuilder.append(stack.pop());
                } catch (EmptyStackException e) {
                    System.err.println("Too few operations in the infix expression.");
                    System.exit(1);
                }
                Character temp;
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
            scan.next();

        } while (scan.hasNext());

        if (!stack.isEmpty()) {
            System.err.println("Expression wasn't fully parenthesized.");
        }

        return stringBuilder.toString();
    }



    public static void main(String[] args) {
        System.out.println(infixToPostfix("(11+2)"));
    }
}
