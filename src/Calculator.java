import javax.swing.*;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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