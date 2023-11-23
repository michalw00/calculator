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
    public static String currentOperator;





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
        ActionListeners.numberListener newNumberListener = new ActionListeners.numberListener();
        ActionListeners.operationListener newOperationListener = new ActionListeners.operationListener();
        ActionListeners.sumListener newSumListener = new ActionListeners.sumListener();
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
        buttonPanel.add(zero);
        buttonPanel.add(one);
        buttonPanel.add(two);
        buttonPanel.add(multiply);
        buttonPanel.add(three);
        buttonPanel.add(four);
        buttonPanel.add(five);
        buttonPanel.add(divide);
        buttonPanel.add(six);
        buttonPanel.add(seven);
        buttonPanel.add(eight);
        buttonPanel.add(plus);
        buttonPanel.add(nine);
        buttonPanel.add(decimalPoint);

        JButton reset = new JButton("Reset");
        JButton clear = new JButton("Clear");
        reset.addActionListener(e -> {
            resultValue = 0.0;
            resultField.setText("0.0");
            operandField.setText("");
            currentOperator = null;
        });
        clear.addActionListener(e -> operandField.setText(""));
        buttonPanel.add(equals);
        buttonPanel.add(minus);
        buttonPanel.add(reset);
        buttonPanel.add(clear);


        add(buttonPanel, BorderLayout.SOUTH);

    }

    public static void numbers(ActionEvent e) {
        String actionCommand = e.getActionCommand();

        if (NUMBERS.contains(Integer.parseInt(actionCommand))) {
            operandField.setText(operandField.getText()+actionCommand);
        }

    }

    public static void operations(ActionEvent e) { // todo
        String actionCommand = e.getActionCommand();
        double operand = Double.parseDouble(operandField.getText());

        if (currentOperator != null) {

            switch (currentOperator) {
                case "+" -> stringBuilder.append("+").append(operand);
                case "-" -> stringBuilder.append("-").append(operand);
                case "*" -> stringBuilder.append("*").append(operand);
                case "/" -> {
                    if (Math.abs(operand) < 1.0e-10) {
                        operandField.setText("Division by zero");
                        return;
                    }
                    stringBuilder.append("/").append(operand);
                }
                default -> operandField.setText("Unexpected error.");
            }
            operandField.setText("");
            resultField.setText(stringBuilder.toString());

        } else stringBuilder.append(operand);

        currentOperator = actionCommand;
        operandField.setText("");
        resultField.setText(stringBuilder.toString()+currentOperator);

    }

    public static void sum(ActionEvent e) { // todo
        String actionCommand = e.getActionCommand();

    }




}
