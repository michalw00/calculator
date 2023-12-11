import javax.swing.*;
import java.awt.*;

public class GraphingCalculatorUI extends CalculatorUI
		implements Calculator.CalculatorGraphingMode, Calculator.CalculatorMode {

	public GraphingCalculatorUI(Calculator calculator, CalculatorState calculatorState) {
		super(calculator, calculatorState);
		initializeCalculatorUI();
	}

	@Override
	public void initializeCalculatorUI() {
		initializeMenu();
		initializeInputPanel();
		initializeButtonGrid();
	}

	@Override
	public void initializeButtonGrid() { // todo
		setButtonPanel(new JPanel());
		GridLayout chosenLayout = new GridLayout(6, 5);
		getButtonPanel().setLayout(chosenLayout);

		String[] buttonLabels =
				{"sin", "7", "8", "9", "/",
				"cos", "4", "5", "6", "*",
				"tan", "1", "2", "3", "-",
				"π",".", "0", "=", "+",
				"x\u00B2", "RESET", "CLEAR", "(", ")",
				"x", "y", };

		for (String label : buttonLabels) {
			char charAt0 = label.charAt(0);
			switch (charAt0) {
				case 'R':
					addButton(getButtonPanel(), label, (e -> {
						resultField.setText("");
						operandField.setText("");
						getCalculatorState().getStringBuilder().setLength(0);
						getCalculatorState().setLastOperator(null);
					}));
					break;
				case 'C':
					addButton(getButtonPanel(), label, (e -> {
						if (getCalculatorState().getLastOperator() != null && getCalculatorState().getLastOperator() != '=')
							operandField.setText("");
					}));
					break;
				case 'π':
					addButton(getButtonPanel(), label, (e -> {
						getCalculatorState().handleErrorState();
						operandField.setText(operandField.getText()+Math.PI);
					}));
					break;
				case 'x':
					addButton(getButtonPanel(), label, (e -> {
						if (!operandField.getText().trim().isEmpty()) {
							getCalculatorState().handleErrorState();
							String temp = operandField.getText();
							operandField.setText(temp + '*' + temp.charAt(temp.length() - 1));
						}
					}));
					break;
				default:
					if (Character.isDigit(charAt0)) {
						addButton(getButtonPanel(), label, new ActionListeners.NumberListener());
						break;
					} else
						addButton(getButtonPanel(), label, new ActionListeners.OperationListener());
					break;
			}
		}

		calculator.add(getButtonPanel(), BorderLayout.SOUTH);
	}

}