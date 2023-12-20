import javax.swing.*;
import java.awt.*;

public class GraphingCalculatorUI extends CalculatorUI
		implements Calculator.CalculatorGraphingMode, Calculator.CalculatorMode {


	public GraphingCalculatorUI(Calculator calculator, CalculatorState calculatorState) {
		super(calculator, calculatorState);
		initializeCalculatorUI();
	}

	@Override
	public void initializeInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		operandField = new JTextField(DIGITS_LIMIT);
		operandField.setEditable(true);
		inputPanel.add(operandField);
		getCalculator().add(inputPanel, BorderLayout.CENTER);
	}

	@Override
	public void initializeCalculatorUI() {
		initializeMenu();
		initializeInputPanel();
		initializeButtonGrid();
		resultField = null;
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
				"π",".", "0", "ENTER", "+",
				"x\u00B2", "RESET", "CLEAR", "(", ")",
				"arg x" };

		for (String label : buttonLabels) {
			char charAt0 = label.charAt(0);
			switch (charAt0) { // todo: error handling of nonsense expressions such as 2+*+
				case 'R':
					addButton(getButtonPanel(), label, (e -> {
						operandField.setText("");
						getCalculator().getGraphWindow().reset();
					}));
					break;
				case 'C':
					addButton(getButtonPanel(), label, e ->
							operandField.setText(""));
					break;
                /*  case "sin":
                    case "cos":
                    case "tan": // todo
                    break; */
				case 'π':
					addButton(getButtonPanel(), label, (e -> {
						getCalculatorState().handleErrorState();
						String temp = operandField.getText();
						operandField.setText(temp+Math.PI);
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
				case 'a':
					addButton(getButtonPanel(), label, e ->
							operandField.setText(operandField.getText()+"x"));
					break;
				case 'E':
					addButton(getButtonPanel(), label, e ->
							getCalculator().getCalculatorModel().plotGraph());
					break;
				default:
					addButton(getButtonPanel(), label, e ->
							operandField.setText(operandField.getText()+label));
					break;
			}
		}

		getCalculator().add(getButtonPanel(), BorderLayout.SOUTH);
	}

}
