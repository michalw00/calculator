import java.util.EmptyStackException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InfixToPostfix {
    public static final Pattern UNSIGNED_DOUBLE = Pattern.compile("(\\d+\\.?\\d*|\\.\\d+([Ee][-+]?\\d+)?)");
    public static final Pattern CHARACTER = Pattern.compile("\\S.*?");
    public static final Pattern OPERATOR = Pattern.compile("[+\\-*/]");

    // todo: space after numbers (currently only works with one digit numbers)
    // todo: handling lacking parenthesis scenario properly


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
                Character tempPop;

                try {
                    if (stack.peek().toString().matches("[+\\-*/]")) {
                        tempPop = stack.pop();
                        temp = stack.peek();
                        boolean isOperator = temp.toString().matches("[+\\-*/]");
                        if (isOperator && firstPrecedes(tempPop, temp)) {
                            stringBuilder.append(tempPop);
                        } else if (isOperator && firstPrecedes(temp, tempPop)) {
                            stringBuilder.append(temp);
                            stack.pop();
                            stack.push(tempPop);
                        } else if (!isOperator) {
                            stringBuilder.append(tempPop);
                        }
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
        System.out.println(infixToPostfix("((1+2)*2)"));
    }
}
