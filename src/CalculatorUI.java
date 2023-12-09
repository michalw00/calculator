import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class CalculatorUI implements Calculator.CalculatorView {
	private static final int WIDTH = 420, HEIGHT = 250;
	public static final int DIGITS_LIMIT = 30;
	public static final Color BACKGROUND_COLOR = new Color(223, 223, 223);
	private GraphWindow graphWindow;

	public static JTextField resultField, operandField;
	public Calculator calculator;
	private final CalculatorState calculatorState;

	private JPanel buttonPanel;



	public CalculatorUI(Calculator calculator, CalculatorState calculatorState) {
		this.calculator = calculator;
		this.calculatorState = calculatorState;

		calculator.setSize(WIDTH, HEIGHT);
		calculator.setResizable(false);
		calculator.setLayout(new BorderLayout());
	}

	public abstract void initializeCalculatorUI();

	public void initializeInputPanel() {
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new FlowLayout());

		operandField = new JTextField(DIGITS_LIMIT/2);
		operandField.setEditable(false);
		inputPanel.add(operandField);
		calculator.add(inputPanel, BorderLayout.CENTER);
	}

	public void initializeMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu dropdownMenu = new JMenu("Mode");

		addMenuItem(dropdownMenu, "Standard", (e -> {
			if (calculator.calculatorUI instanceof GraphingCalculatorUI) {
				graphWindow.setVisible(false);
			}
		}));
		addMenuItem(dropdownMenu, "Graphing", (e -> {
			if (!(calculator.calculatorUI instanceof GraphingCalculatorUI)) {
				graphWindow = new GraphWindow();
				graphWindow.setVisible(true);
				calculator.reset(1);
			}
		}));

		bar.add(dropdownMenu);
		calculator.setJMenuBar(bar);
	}

	//---helper methods---
	public static void addButton(Container container, String label, ActionListener actionListener) {
		JButton button = new JButton(label);
		button.addActionListener(actionListener);
		container.add(button);
	}

	private static void addMenuItem(Container container, String label, ActionListener actionListener) {
		JMenuItem menuItem = new JMenuItem(label);
		menuItem.addActionListener(actionListener);
		container.add(menuItem);
	}
	//--------------------

	public JTextField getOperandField() {
		return operandField;
	}

	public JTextField getResultField() {
		return resultField;
	}

	public CalculatorState getCalculatorState() {
		return calculatorState;
	}

	public JPanel getButtonPanel() {
		return buttonPanel;
	}

	public void setButtonPanel(JPanel buttonPanel) {
		this.buttonPanel = buttonPanel;
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
