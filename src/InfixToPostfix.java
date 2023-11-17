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
        Scanner scan = new Scanner(expression);
        StringBuilder stringBuilder = new StringBuilder();
        String next;

        do {

            // I had to use Matcher for more complex patterns, as something didn't work previously when I tried to match with Scanner.
            if (scan.hasNext("\\(")) {
                stack.push(scan.findInLine("\\(").charAt(0));
                scan.next();
            } else if (matcherNumbers.find()) {
                next = matcherNumbers.group();
                stringBuilder.append(next);
                scan.next();
            } else if (matcherOperators.find()) {
                next = matcherOperators.group();
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
                Character temp;
                try {
                    temp = stack.peek();
                    if (temp == '(') { // todo: this might never be matched, need to look into it
                        stack.pop();
                    }
                } catch (EmptyStackException e) {
                    //System.err.println("No balanced parentheses.");
                    //System.exit(1);
                }
            }

        } while (scan.hasNext()); // todo: expression characters need to be separated by spaces for this to work, need to fix this

        if (!stack.isEmpty()) {
            System.err.println("Expression wasn't fully parenthesized.");
        }

        return stringBuilder.toString();
    }



    public static void main(String[] args) {
        System.out.println(infixToPostfix("( 1 + 2 )"));
    }
}
