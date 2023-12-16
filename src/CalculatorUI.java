import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public abstract class CalculatorUI implements Calculator.CalculatorView {
	private static final int WIDTH = 420, HEIGHT = 250;
	public static final int DIGITS_LIMIT = 30;
	public static final Color BACKGROUND_COLOR = new Color(223, 223, 223);

	public JTextField resultField, operandField;
	private JPanel buttonPanel;
	private Calculator calculator;
	private CalculatorState calculatorState;




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
			if (calculator.getCalculatorUI() instanceof GraphingCalculatorUI) {
				reset(calculator, 0);
			}
		}));
		addMenuItem(dropdownMenu, "Graphing", (e -> {
			if (!(calculator.getCalculatorUI() instanceof GraphingCalculatorUI)) {
				reset(calculator, 1);
				calculator.setGraphWindow(new GraphingCalculatorUI.GraphWindow());
				calculator.getGraphWindow().setVisible(true);
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

	public class GraphWindow extends JFrame {
		public static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 500;
		private ArrayList<Line> lines = new ArrayList<>();

		public GraphWindow() {
			super("Graph");
			setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
			setResizable(false);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			getContentPane().setBackground(Color.WHITE);
			//setLocationRelativeTo(null);
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

			for (Line line : lines) {
				g.drawLine(line.x1, (int) line.y1, line.x2, (int) line.y2);
			}

			calculatorState.setGraphicsState(g);
		}

		public void addLine(int lastX, double lastY, int currentX, double currentY) { // todo

			int intValueOfY = (int) Math.round(currentY);
			int intValueOfLastY = (int) Math.round(lastY);

			lines.add(new Line(lastX, intValueOfLastY, currentX, intValueOfY));
		}

		private static class Line {
			int x1, x2;
			double y1, y2;

			public Line(int x1, double y1, int x2, double y2) {
				this.x1 = x1;
				this.y1 = y1;
				this.x2 = x2;
				this.y2 = y2;
			}
		}

	}

	//---UI utils---

	public void reset(Calculator calculator, int newMode) {
		if (newMode == 0 && calculator.getGraphWindow() != null) {
			calculator.getGraphWindow().dispose();
			calculator.setCalculatorUI(null);
		}
		calculator.getContentPane().removeAll();
		initialize(calculator, newMode);
		calculator.revalidate();
		calculator.repaint();
	}

	public static void initialize(Calculator calculator, int mode) {
		calculator.setCalculatorState(new CalculatorState(calculator));

		switch (mode) {
			case 0 -> calculator.setCalculatorUI
					(new StandardCalculatorUI(calculator, calculator.getCalculatorState()));
			case 1 -> calculator.setCalculatorUI
					(new GraphingCalculatorUI(calculator, calculator.getCalculatorState()));
			default -> {
				System.err.println("This shouldn't have happened.");
				System.exit(1);
			}
		}

		calculator.setCalculatorModel
				(new CalculatorModel(calculator, calculator.getCalculatorState(), calculator.getCalculatorUI()));
		calculator.setActionListeners
				(new ActionListeners(calculator.getCalculatorModel()));
	}

	//---boilerplate stuff---

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

	public Calculator getCalculator() {
		return calculator;
	}

}
