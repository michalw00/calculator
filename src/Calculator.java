import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Calculator extends JFrame {
    public static final int WIDTH = 300, HEIGHT = 230, NUMBER_OF_DIGITS = 20;
    public static final ArrayList<Integer> NUMBERS = new ArrayList<>();

    public static StringBuilder stringBuilder = new StringBuilder();
    public static JTextField resultField, operandField;
    public static Character lastOperator;
    public static boolean errorState = false;




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

        initializeResultPanel();
        initializeInputPanel();
        initializeButtonGrid();
    }

    private void initializeResultPanel() {
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout());

        resultField = new JTextField("", NUMBER_OF_DIGITS);
        resultField.setBackground(Color.WHITE);
        textPanel.add(resultField);
        add(textPanel, BorderLayout.NORTH);
    }
    private void initializeInputPanel() {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        operandField = new JTextField(NUMBER_OF_DIGITS);
        operandField.setBackground(Color.WHITE);
        inputPanel.add(operandField);
        add(inputPanel, BorderLayout.CENTER);
    }
    private void initializeButtonGrid() {
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

        ActionListeners.NumberListener newNumberListener =
                new ActionListeners.NumberListener();
        ActionListeners.OperationListener newOperationListener =
                new ActionListeners.OperationListener();

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
        equals.addActionListener(newOperationListener);

        // row I
        buttonPanel.add(seven);
        buttonPanel.add(eight);
        buttonPanel.add(nine);
        buttonPanel.add(divide);

        // row II
        buttonPanel.add(four);
        buttonPanel.add(five);
        buttonPanel.add(six);
        buttonPanel.add(multiply);

        // row III
        buttonPanel.add(one);
        buttonPanel.add(two);
        buttonPanel.add(three);
        buttonPanel.add(minus);

        // row IV
        buttonPanel.add(decimalPoint);
        buttonPanel.add(zero);
        JButton reset = new JButton("Reset");
        JButton clear = new JButton("Clear");
        reset.addActionListener(e -> {
            resultField.setText("");
            operandField.setText("");
            stringBuilder.setLength(0);
            lastOperator = null;
        });
        clear.addActionListener(e -> {
            if (lastOperator != null && lastOperator != '=')
                operandField.setText("");
        });
        leftBracket.addActionListener(newOperationListener);
        rightBracket.addActionListener(newOperationListener);
        buttonPanel.add(equals);
        buttonPanel.add(plus);

        // row V
        buttonPanel.add(reset);
        buttonPanel.add(clear);
        buttonPanel.add(leftBracket);
        buttonPanel.add(rightBracket);


        add(buttonPanel, BorderLayout.SOUTH);
    }





    public static void numbers(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (errorState) {
            operandField.setText("");
            errorState = false;
        }

        if (NUMBERS.contains(Integer.parseInt(actionCommand))) {
            operandField.setText(operandField.getText()+actionCommand);
        }
    }

    public static void operations(String actionCommand) {
        String temp = resultField.getText();
        //if (isLastCharacterOperator(temp) || (Double.parseDouble(temp) == 0.0 && operandField.getText().trim().isEmpty()) ) return; //todo. Double.parseDouble(temp) throws an error if there is parenthesis present


        lastOperator = actionCommand.charAt(0);

        stringBuilder.append(operandField.getText()).append(actionCommand);
        operandField.setText("");
        resultField.setText(stringBuilder.toString());
    }

    public static void sum() {
        if (lastOperator == null || lastOperator == '=') return;


        String operandFieldText = operandField.getText();
        if (lastOperator == '/' && Math.abs(Double.parseDouble(operandFieldText)) < 1.0e-10) {
            operandField.setText("ERROR: Division by zero!");
            errorState = true;
            return;
        }
        lastOperator = '=';

        String resultToString = resultField.getText();
        resultToString += operandFieldText;
        String newOperandField =
                Double.toString(InfixToPostfix.evaluatePostfix(InfixToPostfix.infixToPostfix(resultToString)));
        operandField.setText("");

        resultField.setText(resultField.getText()+operandFieldText+"=");
        operandField.setText(newOperandField);
        stringBuilder.setLength(0);
    }

    public static void parenthesis(String actionCommand) {
        // if (actionCommand is ')' and there is no '(' in result field) return;
        String unsignedDouble = "(\\d+\\.?\\d*|\\.\\d+([Ee][-+]?\\d+)?)";

        lastOperator = actionCommand.charAt(0);

        String tempOperand = operandField.getText();
        String tempResult = resultField.getText();

        //  Condition 1:
        // If either the result field or operand field is not empty,
        // and the current operand is a valid double and not a closing parenthesis.
        boolean multiplicationCondition1 = (!isResultFieldEmpty() || !isOperandFieldEmpty()) &&
                (tempOperand.matches(unsignedDouble) && actionCommand.charAt(0) != ')');
        //  Condition 2:
        // If the operand field is empty, the result field is not empty,
        // the current action is an opening parenthesis, and the last character of the result is not an opening parenthesis.
        boolean multiplicationCondition2 = (isOperandFieldEmpty() && !isResultFieldEmpty() &&
                actionCommand.charAt(0) == '(' && tempResult.charAt(tempResult.length() - 1) != '(');
        if (multiplicationCondition1 || multiplicationCondition2) {
            stringBuilder.append(operandField.getText()).append('*').append(actionCommand);
        } else stringBuilder.append(operandField.getText()).append(actionCommand);

        operandField.setText("");
        resultField.setText(stringBuilder.toString());
    }

    //---helper methods---
    private static boolean isLastCharacterOperator(String resultToString) {
        if (isResultFieldEmpty())
            return false;
        char lastCharacterOfResult = resultToString.charAt(resultToString.length() - 1);
        return String.valueOf(lastCharacterOfResult).matches(InfixToPostfix.OPERATOR.toString());
    }
    private static boolean isResultFieldEmpty() {
        return resultField.getText().trim().isEmpty();
    }
    private static boolean isOperandFieldEmpty() {
        return operandField.getText().trim().isEmpty();
    }
    private static void updateResultFieldWithChar(char characterToAppend) {
        stringBuilder.append(characterToAppend);
        resultField.setText(stringBuilder.toString());
    }
    //---------------------
}