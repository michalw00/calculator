import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CalculatorUI implements Calculator.CalculatorView {
	public static final int WIDTH = 420, HEIGHT = 250;
	public static final int NUMBER_OF_DIGITS = 30;
	private GraphWindow graphWindow;

	public static JTextField resultField, operandField;
	public Calculator calculator;
	private final CalculatorState calculatorState;

	public CalculatorUI(Calculator calculator, CalculatorState calculatorState) {
		this.calculator = calculator;
		this.calculatorState = calculatorState;
		initializeCalculatorUI();
	}

	public void initializeCalculatorUI() {
		calculator.setSize(WIDTH, HEIGHT);
		calculator.setResizable(false);
		calculator.setLayout(new BorderLayout());

		initializeResultPanel();
		initializeInputPanel();
		initializeButtonGrid();
		initializeMenu();
	}

	public void initializeResultPanel() {
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new FlowLayout());

		resultField = new JTextField("", NUMBER_OF_DIGITS);
		resultField.setBackground(Color.WHITE);
		resultField.setEditable(false);
		resultField.setBackground(new Color(223, 223, 223));
		textPanel.add(resultField);
		calculator.add(textPanel, BorderLayout.NORTH);
	}

	public void initializeInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		operandField = new JTextField(NUMBER_OF_DIGITS/2);
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
			switch (charAt0) {
				case 'R':
					addButton(buttonPanel, label, (e -> {
						resultField.setText("");
						operandField.setText("");
						calculatorState.getStringBuilder().setLength(0);
						calculatorState.setLastOperator(null);
					}));
					break;
				case 'C':
					addButton(buttonPanel, label, (e -> {
						if (calculatorState.getLastOperator() != null && calculatorState.getLastOperator() != '=')
							operandField.setText("");
					}));
					break;
				case 'π':
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

		addMenuItem(dropdownMenu, "Standard", (e -> {

			if (calculatorState.isGraphModeOn()) {
				calculatorState.setGraphMode(false);
				graphWindow.setVisible(false);
			}
		}));
		addMenuItem(dropdownMenu, "Graphing", (e -> {
			if (!calculatorState.isGraphModeOn()) {
				calculatorState.setGraphMode(true);
				graphWindow = new GraphWindow();
				graphWindow.setVisible(true);
			}
		}));

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





	public class GraphWindow extends JFrame {
		public static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 500;

		public GraphWindow() {
			super("Graph");
			setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			setResizable(false);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			getContentPane().setBackground(Color.WHITE);
		}

		public void paint(Graphics g) {
			super.paint(g);

			// x-axis
			g.drawLine(0, WINDOW_HEIGHT / 2, WINDOW_WIDTH, WINDOW_HEIGHT / 2);

			// y-axis
			g.drawLine(WINDOW_WIDTH / 2, 0, WINDOW_WIDTH / 2, WINDOW_HEIGHT);

			// axis labels
			g.drawString("X", WINDOW_WIDTH - 20, WINDOW_HEIGHT / 2 + 15);
			g.drawString("Y", WINDOW_WIDTH / 2 + 5, 45);
		}
	}

}
