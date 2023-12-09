import javax.swing.*;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {
    CalculatorState calculatorState;
    CalculatorUI calculatorUI;
    CalculatorModel calculatorModel;
    ActionListeners actionListeners;

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initialize(0);
    }

    public void reset(int newMode) {
        getContentPane().removeAll();
        initialize(newMode);
        revalidate();
        repaint();
    }

    private void initialize(int mode) {
        calculatorState = new CalculatorState();
        switch (mode) {
            case 0 -> calculatorUI = new StandardCalculatorUI(this, calculatorState);
            case 1 -> calculatorUI = new GraphingCalculatorUI(this, calculatorState);
            default -> {
                System.err.println("This shouldn't have happened.");
                System.exit(1);
            }
        }
        calculatorModel = new CalculatorModel(this, calculatorState, calculatorUI);
        actionListeners = new ActionListeners(calculatorModel);
    }





    public interface CalculatorView {
        void initializeMenu();
    }

    public interface CalculatorMode extends CalculatorView {
        void initializeInputPanel();
        void initializeButtonGrid();
    }

    public interface CalculatorStandardMode extends CalculatorMode {
        void initializeResultPanel();
    }

    public interface CalculatorGraphingMode extends CalculatorMode {
        // ...additional methods
    }

    public interface CalculatorOperations {
        void handleNumberInput(ActionEvent e);
        void handleOperationInput(String actionCommand);
        void performSum();
        void handleParenthesis(String actionCommand);
    }
}