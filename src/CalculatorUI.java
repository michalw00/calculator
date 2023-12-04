import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatorUI implements Calculator.CalculatorView {
	public static JTextField resultField, operandField;
	public Calculator calculator;
	private final CalculatorState calculatorState;

	public static StringBuilder stringBuilder = new StringBuilder();
	public static Character lastOperator;

	public CalculatorUI(Calculator calculator, CalculatorState calculatorState) {
		this.calculator = calculator;
		this.calculatorState = calculatorState;
		initializeCalculatorUI();
	}

	public void initializeCalculatorUI() {
		initializeResultPanel();
		initializeInputPanel();
		initializeButtonGrid();
		initializeMenu();
	}

	public void initializeResultPanel() {
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());

		resultField = new JTextField("", Calculator.NUMBER_OF_DIGITS);
		resultField.setBackground(Color.WHITE);
		resultField.setEditable(false);
		resultField.setBackground(new Color(223, 223, 223));
		textPanel.add(resultField);
		calculator.add(textPanel, BorderLayout.NORTH);
	}

	public void initializeInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		operandField = new JTextField(Calculator.NUMBER_OF_DIGITS/2);
		operandField.setEditable(false);
		inputPanel.add(operandField);
		calculator.add(inputPanel, BorderLayout.CENTER);
	}

	public void initializeButtonGrid() {
		JPanel buttonPanel = new JPanel();
		GridLayout chosenLayout = new GridLayout(5, 5);
		buttonPanel.setLayout(chosenLayout);

		String[] buttonLabels =
				{"sin", "7", "8", "9", "/",
						"cos", "4", "5", "6", "*",
						"tan", "1", "2", "3", "-",
						"π",".", "0", "=", "+",
						"x\u00B2", "RESET", "CLEAR", "(", ")"};

		for (String label : buttonLabels) {
			char charAt0 = label.charAt(0);
			switch (label) {
				case "RESET":
					addButton(buttonPanel, label, (e -> {
						resultField.setText("");
						operandField.setText("");
						stringBuilder.setLength(0);
						lastOperator = null;
					}));
					break;
				case "CLEAR":
					addButton(buttonPanel, label, (e -> {
						if (lastOperator != null && lastOperator != '=')
							operandField.setText("");
					}));
					break;
				case "π":
					addButton(buttonPanel, label, (e -> {
						calculatorState.handleErrorState();
						operandField.setText(operandField.getText()+Math.PI);
					}));
					break;
                /*case "sin":
                case "cos":
                case "tan": // todo
                    break; */
				default:
					if (Character.isDigit(charAt0)) {
						addButton(buttonPanel, label, new ActionListeners.NumberListener());
						break;
					} else
						addButton(buttonPanel, label, new ActionListeners.OperationListener());
					break;
			}
		}

		calculator.add(buttonPanel, BorderLayout.SOUTH);
	}

	public void initializeMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu dropdownMenu = new JMenu("Mode");

		addMenuItem(dropdownMenu, "Standard", null);
		addMenuItem(dropdownMenu, "Graphing", null);

		bar.add(dropdownMenu);
		calculator.setJMenuBar(bar);
	}

	//---helper methods---
	private static void addButton(Container container, String label, ActionListener actionListener) {
		JButton button = new JButton(label);
		button.addActionListener(actionListener);
		container.add(button);
	}

	private static void addMenuItem(Container container, String label, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(label);
		menuItem.addActionListener(actionListener);
		container.add(menuItem);
	}

	//----------------
	public JTextField getOperandField() {
		return operandField;
	}

	public JTextField getResultField() {
		return resultField;
	}
}
