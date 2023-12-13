import java.awt.event.ActionEvent;
import java.util.ArrayList;

public class CalculatorModel implements Calculator.CalculatorOperations {
	public static final ArrayList<Integer> NUMBERS = new ArrayList<>();

	public Calculator calculator;
	private final CalculatorState calculatorState;
	private final CalculatorUI calculatorUI;


	public CalculatorModel(Calculator calculator, CalculatorState calculatorState, CalculatorUI calculatorUI) {
		for (int i = 0; i <= 9; i++) NUMBERS.add(i);

		this.calculator = calculator;
		this.calculatorState = calculatorState;
		this.calculatorUI = calculatorUI;
	}

	public void handleNumberInput(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		calculatorState.handleErrorState();
		if (NUMBERS.contains(Integer.parseInt(actionCommand))) {
			calculatorUI.getOperandField().setText(calculatorUI.getOperandField().getText()+actionCommand);
		}
	}

	public void handleOperationInput(String actionCommand) {
		calculatorState.setLastOperator(actionCommand.charAt(0));

		calculatorState.getStringBuilder().append(calculatorUI.getOperandField().getText()).append(actionCommand);
		calculatorUI.getOperandField().setText("");
		calculatorUI.getResultField().setText(calculatorState.getStringBuilder().toString());
	}

	public void performSum() {
		if (calculatorState.getLastOperator() == null || calculatorState.getLastOperator() == '=')
			return;

		String operandFieldText = calculatorUI.getOperandField().getText();
		String resultFieldText = calculatorUI.getResultField().getText();
		if (String.valueOf(resultFieldText.charAt(resultFieldText.length() - 1)).matches("[+\\-*/]")
				&& calculatorUI.getOperandField().getText().trim().isEmpty())
			return;

		if (calculatorState.getLastOperator() == '/' && Math.abs(Double.parseDouble(operandFieldText)) < 1.0e-10) {
			calculatorUI.getOperandField().setText("ERROR: Division by zero!");
			calculatorState.setErrorState(true);
			return;
		}
		calculatorState.setLastOperator('=');

		String resultToString = calculatorUI.getResultField().getText();
		resultToString += operandFieldText;
		String newOperandField =
				Double.toString(InfixToPostfix.evaluatePostfix(InfixToPostfix.infixToPostfix(resultToString)));
		calculatorUI.getOperandField().setText("");

		calculatorUI.getResultField().setText(calculatorUI.getResultField().getText()+operandFieldText+"=");
		calculatorUI.getOperandField().setText(newOperandField);
		calculatorState.getStringBuilder().setLength(0);
	}

	public void plotGraph() {
		double middleWidth = CalculatorUI.GraphWindow.WINDOW_HEIGHT / 2;

		String operandFieldText = calculatorUI.getOperandField().getText();
		String expression = InfixToPostfix.replaceVariableWithArgumentValue(operandFieldText, 0);

		double currentY = InfixToPostfix.evaluatePostfix(InfixToPostfix.infixToPostfix(expression)) * middleWidth;
		int lastX = 0;
		double lastY = currentY;
		for (int currentX = 1; currentX < GraphingCalculatorUI.GraphWindow.WINDOW_WIDTH; currentX++) {
			expression = InfixToPostfix.replaceVariableWithArgumentValue(operandFieldText, currentX);
			currentY = InfixToPostfix.evaluatePostfix(InfixToPostfix.infixToPostfix(expression)) * middleWidth;
			calculator.getGraphWindow().paint(calculatorState.getGraphicsState(), lastX, lastY, currentX, currentY);

			lastX = currentX;
			lastY = currentY;
		}

	}

	public void handleParenthesis(String actionCommand) {
		// todo: if (actionCommand is ')' and there is no '(' in result field) return;
		// ^ technically this should be checked in the InfixToPostfix class, double check

		calculatorState.setLastOperator(actionCommand.charAt(0));

		String tempOperand = calculatorUI.getOperandField().getText();
		String tempResult = calculatorUI.getResultField().getText();

		//  Condition 1:
		// If either the result field or operand field is not empty,
		// and the current operand is a valid double and not a closing parenthesis.
		String unsignedDouble = "(\\d+\\.?\\d*|\\.\\d+([Ee][-+]?\\d+)?)";
		boolean multiplicationCondition1 = (!isResultFieldEmpty() || !isOperandFieldEmpty()) &&
				(tempOperand.matches(unsignedDouble) && actionCommand.charAt(0) != ')');
		//  Condition 2:
		// If the operand field is empty, the result field is not empty,
		// the current action is an opening parenthesis, and the last character of the result is not an opening parenthesis.
		boolean multiplicationCondition2 = (isOperandFieldEmpty() && !isResultFieldEmpty() &&
				actionCommand.charAt(0) == '(' && tempResult.charAt(tempResult.length() - 1) != '(');
		if (multiplicationCondition1 || multiplicationCondition2) {
			calculatorState.getStringBuilder().append(calculatorUI.getOperandField().getText()).append('*').append(actionCommand);
		} else calculatorState.getStringBuilder().append(calculatorUI.getOperandField().getText()).append(actionCommand);

		calculatorUI.getOperandField().setText("");
		calculatorUI.getResultField().setText(calculatorState.getStringBuilder().toString());
	}




	//---helper methods---

	private boolean isResultFieldEmpty() {
		return calculatorUI.getResultField().getText().trim().isEmpty();
	}

	private boolean isOperandFieldEmpty() {
		return calculatorUI.getOperandField().getText().trim().isEmpty();
	}

}
