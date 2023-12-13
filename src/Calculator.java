import javax.swing.*;
import java.awt.event.ActionEvent;

public class Calculator extends JFrame {
    private CalculatorState calculatorState;
    private CalculatorUI calculatorUI;
    private GraphingCalculatorUI.GraphWindow graphWindow;
    private CalculatorModel calculatorModel;
    private ActionListeners actionListeners;

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.setVisible(true);
    }

    public Calculator() {
        super("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        CalculatorUI.initialize(this, 0);
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
        // todo: additional methods
    }

    public interface CalculatorOperations {
        void handleNumberInput(ActionEvent e);
        void handleOperationInput(String actionCommand);
        void performSum();
        void handleParenthesis(String actionCommand);
    }




    //---boilerplate stuff---

    public CalculatorState getCalculatorState() {
        return calculatorState;
    }

    public CalculatorUI getCalculatorUI() {
        return calculatorUI;
    }

    public CalculatorModel getCalculatorModel() {
        return calculatorModel;
    }

    public ActionListeners getActionListeners() {
        return actionListeners;
    }

    public void setCalculatorState(CalculatorState calculatorState) {
        this.calculatorState = calculatorState;
    }

    public void setCalculatorUI(CalculatorUI calculatorUI) {
        this.calculatorUI = calculatorUI;
    }

    public void setCalculatorModel(CalculatorModel calculatorModel) {
        this.calculatorModel = calculatorModel;
    }

    public void setActionListeners(ActionListeners actionListeners) {
        this.actionListeners = actionListeners;
    }

    public GraphingCalculatorUI.GraphWindow getGraphWindow() {
        return graphWindow;
    }

    public void setGraphWindow(GraphingCalculatorUI.GraphWindow graphWindow) {
        this.graphWindow = graphWindow;
    }
}