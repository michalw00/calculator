import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Calculator extends JFrame {
    public static final int WIDTH = 300;
    public static final int HEIGHT = 230;
    public static final int NUMBER_OF_DIGITS = 20;
    public static final ArrayList<Integer> NUMBERS = new ArrayList<>();

    public static StringBuilder stringBuilder = new StringBuilder();
    public static JTextField resultField;
    public static JTextField operandField;
    public static double resultValue = 0.0;
    public static String lastOperator; // need to clear this on reset




    public static void main(String[] args) {
        Calculator aCalculator = new Calculator();
        aCalculator.setVisible(true);
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLayout(new BorderLayout());
        for (int i = 0; i <= 9; i++) NUMBERS.add(i);

        // result panel
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout());
        resultField = new JTextField("Result: "+ resultValue, NUMBER_OF_DIGITS);
        resultField.setBackground(Color.WHITE);
        textPanel.add(resultField);
        add(textPanel, BorderLayout.NORTH);

        // input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());
        operandField = new JTextField(NUMBER_OF_DIGITS);
        operandField.setBackground(Color.WHITE);
        inputPanel.add(operandField);
        add(inputPanel, BorderLayout.CENTER);

        // button grid
        JPanel buttonPanel = new JPanel();
        GridLayout chosenLayout = new GridLayout(5, 4);
        buttonPanel.setLayout(chosenLayout);
        JButton zero = new JButton("0");
        JButton one = new JButton("1");
        JButton two = new JButton("2");
        JButton three = new JButton("3");
        JButton four = new JButton("4");
        JButton five = new JButton("5");
        JButton six = new JButton("6");
        JButton seven = new JButton("7");
        JButton eight = new JButton("8");
        JButton nine = new JButton("9");
        JButton multiply = new JButton("*");
        JButton divide = new JButton("/");
        JButton plus = new JButton("+");
        JButton minus = new JButton("-");
        JButton decimalPoint = new JButton(".");
        JButton equals = new JButton("=");
        JButton leftBracket = new JButton("(");
        JButton rightBracket = new JButton(")");
        ActionListeners.NumberListener newNumberListener = new ActionListeners.NumberListener();
        ActionListeners.OperationListener newOperationListener = new ActionListeners.OperationListener();
        ActionListeners.SumListener newSumListener = new ActionListeners.SumListener();
        ActionListeners.ParenthesisListener newParenthesisListener = new ActionListeners.ParenthesisListener();
        zero.addActionListener(newNumberListener);
        one.addActionListener(newNumberListener);
        two.addActionListener(newNumberListener);
        three.addActionListener(newNumberListener);
        four.addActionListener(newNumberListener);
        five.addActionListener(newNumberListener);
        six.addActionListener(newNumberListener);
        seven.addActionListener(newNumberListener);
        eight.addActionListener(newNumberListener);
        nine.addActionListener(newNumberListener);
        multiply.addActionListener(newOperationListener);
        divide.addActionListener(newOperationListener);
        plus.addActionListener(newOperationListener);
        minus.addActionListener(newOperationListener);
        decimalPoint.addActionListener(newOperationListener);
        equals.addActionListener(newSumListener);


        buttonPanel.add(seven);
        buttonPanel.add(eight);
        buttonPanel.add(nine);
        buttonPanel.add(divide);
        buttonPanel.add(four);
        buttonPanel.add(five);
        buttonPanel.add(six);
        buttonPanel.add(multiply);

        buttonPanel.add(one);
        buttonPanel.add(two);
        buttonPanel.add(three);
        buttonPanel.add(minus);

        buttonPanel.add(decimalPoint);
        buttonPanel.add(zero);



        JButton reset = new JButton("Reset");
        JButton clear = new JButton("Clear");
        reset.addActionListener(e -> {
            resultValue = 0.0;
            resultField.setText("0.0");
            operandField.setText("");
            stringBuilder.setLength(0);
            lastOperator = null;
        });
        clear.addActionListener(e -> operandField.setText(""));
        leftBracket.addActionListener(newParenthesisListener);
        rightBracket.addActionListener(newParenthesisListener);
        buttonPanel.add(equals);
        buttonPanel.add(plus);
        buttonPanel.add(reset);
        buttonPanel.add(clear);
        buttonPanel.add(leftBracket);
        buttonPanel.add(rightBracket);


        add(buttonPanel, BorderLayout.SOUTH);

    }

    public static void numbers(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (NUMBERS.contains(Integer.parseInt(actionCommand))) {
            operandField.setText(operandField.getText()+actionCommand);
        }

    }

    public static void operations(ActionEvent e) { // todo, reset button doesn't handle string builder correctly
        String actionCommand = e.getActionCommand();

        String operand;
        String resultToString = resultField.getText();
        if (isLastCharacterOperator(resultToString) && !stringBuilder.isEmpty()) {
            operand = "";
        } else {
            operand = operandField.getText();
        }

        lastOperator = actionCommand;
        stringBuilder.append(operand).append(actionCommand);
        operandField.setText("");
        resultField.setText(stringBuilder.toString());

    }

    public static void sum() { //todo
        String resultToString = resultField.getText();
        if (isLastCharacterOperator(resultToString)) {
            return;
        }
        String operandFieldText = operandField.getText();
        if (lastOperator.charAt(0) == '/' && Math.abs(Double.parseDouble(operandFieldText)) < 1.0e-10) {
            operandField.setText("ERROR: Division by zero!");
            return;
        }
        resultToString += operandFieldText;
        String newOperandField = Double.toString(InfixToPostfix.evaluatePostfix(InfixToPostfix.infixToPostfix(resultToString)));
        operandField.setText("");

        resultField.setText(resultField.getText()+operandFieldText+"=");
        operandField.setText(newOperandField);
        stringBuilder.setLength(0);
        stringBuilder.append(newOperandField);
    }

    public static void addParenthesis(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        stringBuilder.append(actionCommand);
        resultField.setText(stringBuilder.toString());
    }

    private static boolean isLastCharacterOperator(String resultToString) {
        char lastCharacterOfResult = resultToString.charAt(resultToString.length() - 1);
        return String.valueOf(lastCharacterOfResult).matches(InfixToPostfix.OPERATOR.toString()) && String.valueOf(lastCharacterOfResult).charAt(0) != '(' && String.valueOf(lastCharacterOfResult).charAt(0) != ')';
    }
}