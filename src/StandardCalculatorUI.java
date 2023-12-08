import javax.swing.*;
import java.awt.*;

public class StandardCalculatorUI extends CalculatorUI implements Calculator.CalculatorStandardMode {

	public StandardCalculatorUI(Calculator calculator, CalculatorState calculatorState) {
		super(calculator, calculatorState);
	}

	@Override
	public void initializeCalculatorUI() {
		initializeMenu();
		initializeInputPanel();
		initializeResultPanel();
		initializeButtonGrid();
	}

	public void initializeResultPanel() {
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());

		resultField = new JTextField("", DIGITS_LIMIT);
		resultField.setBackground(Color.WHITE);
		resultField.setEditable(false);
		resultField.setBackground(BACKGROUND_COLOR);
		textPanel.add(resultField);
		calculator.add(textPanel, BorderLayout.NORTH);
	}

	public void initializeButtonGrid() {
		setButtonPanel(new JPanel());
		GridLayout chosenLayout = new GridLayout(5, 5);
		getButtonPanel().setLayout(chosenLayout);

		String[] buttonLabels =
				{"sin", "7", "8", "9", "/",
						"cos", "4", "5", "6", "*",
						"tan", "1", "2", "3", "-",
						"π",".", "0", "=", "+",
						"x\u00B2", "RESET", "CLEAR", "(", ")"};

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
                /*case "sin":
                case "cos":
                case "tan": // todo
                    break; */
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
