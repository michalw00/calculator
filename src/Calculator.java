import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class Calculator extends JFrame {
    public static final int WIDTH = 420, HEIGHT = 250, NUMBER_OF_DIGITS = 30;
    public static final ArrayList<Integer> NUMBERS = new ArrayList<>();

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(new BorderLayout());
        for (int i = 0; i <= 9; i++) NUMBERS.add(i);

        CalculatorState calculatorState;
        CalculatorUI calculatorUI;
        CalculatorModel calculatorModel;
        ActionListeners actionListeners;

        calculatorState = new CalculatorState();
        calculatorUI = new CalculatorUI(this, calculatorState);
        calculatorModel = new CalculatorModel(this, calculatorState, calculatorUI);
        actionListeners = new ActionListeners(calculatorModel);


    }

    public interface CalculatorView {
        void initializeResultPanel();
        void initializeInputPanel();
        void initializeButtonGrid();
        void initializeMenu();
    }

    public interface CalculatorOperations {
        void handleNumberInput(ActionEvent e);
        void handleOperationInput(String actionCommand);
        void performSum();
        void handleParenthesis(String actionCommand);
    }
}