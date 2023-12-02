import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Calculator extends JFrame {
    public static final Pattern UNSIGNED_DOUBLE = Pattern.compile("(\\d+\\.?\\d*|\\.\\d+([Ee][-+]?\\d+)?)");
    public static final Pattern CHARACTER = Pattern.compile("\\S.*?");
    public static final Pattern OPERATOR = Pattern.compile("[+\\-*/]");

    public static final int WIDTH = 600, HEIGHT = 230, NUMBER_OF_DIGITS = 20;
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
        initializeTrigonometryPanel();
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
    private void initializeTrigonometryPanel() { // todo: add constructor for these kind of panels maybe? So something like makeNewPanel(5 rows, 4 columns, String[] arrayContainingButtonLabels)
        JPanel trigonometryPanel = new JPanel();
        GridLayout chosenLayout = new GridLayout(1, 3);
        trigonometryPanel.setLayout(chosenLayout);
        JButton sin = new JButton("sin");
        JButton cos = new JButton("cos");
        JButton tan = new JButton("tan");

        ActionListeners.TrigonometryListener newTrigonometryFunctionListener =
                new ActionListeners.TrigonometryListener();

        sin.addActionListener(newTrigonometryFunctionListener);
        cos.addActionListener(newTrigonometryFunctionListener);
        tan.addActionListener(newTrigonometryFunctionListener);

        trigonometryPanel.add(sin);
        trigonometryPanel.add(cos);
        trigonometryPanel.add(tan);
        add(trigonometryPanel, BorderLayout.EAST);
    }
    private void initializeButtonGrid() {
        JPanel buttonPanel = new JPanel();
        GridLayout chosenLayout = new GridLayout(5, 4);
        buttonPanel.setLayout(chosenLayout);

        String[] buttonLabels =
                {"7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                ".", "0", "=", "+",
                "RESET", "CLEAR", "(", ")"};

        for (String label : buttonLabels) {
            char charAt0 = label.charAt(0);
            switch (charAt0) {
                case 'R':
                    addButton(buttonPanel, label, (e -> {
                        resultField.setText("");
                        operandField.setText("");
                        stringBuilder.setLength(0);
                        lastOperator = null;
                    }));
                    break;
                case 'C':
                    addButton(buttonPanel, label, (e -> {
                        if (lastOperator != null && lastOperator != '=')
                            operandField.setText("");
                    }));
                    break;
                default:
                    if (Character.isDigit(charAt0)) {
                        addButton(buttonPanel, label, new ActionListeners.NumberListener());
                        break;
                    } else
                        addButton(buttonPanel, label, new ActionListeners.OperationListener());
                    break;
            }
        }

        add(buttonPanel, BorderLayout.SOUTH);
    }




    //---event handlers---
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
    //--------------------


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

    private static void addButton(Container container, String label, ActionListener actionListener) {
        JButton button = new JButton(label);
        button.addActionListener(actionListener);
        container.add(button);
    }
    //--------------------
}