import java.awt.event.ActionEvent;

public class CalculatorModel implements Calculator.CalculatorOperations {
	public Calculator calculator;
	private final CalculatorState calculatorState;
	private final CalculatorUI calculatorUI;


	public CalculatorModel(Calculator calculator, CalculatorState calculatorState, CalculatorUI calculatorUI) {
		this.calculator = calculator;
		this.calculatorState = calculatorState;
		this.calculatorUI = calculatorUI;
	}

	public void handleNumberInput(ActionEvent e) {
		String actionCommand = e.getActionCommand();

		calculatorState.handleErrorState();
		if (calculator.NUMBERS.contains(Integer.parseInt(actionCommand))) {
			calculatorUI.getOperandField().setText(calculatorUI.getOperandField().getText()+actionCommand);
		}
	}

	public void handleOperationInput(String actionCommand) {
		String temp = calculatorUI.getResultField().getText();
		//if (isLastCharacterOperator(temp) || (Double.parseDouble(temp) == 0.0 && operandField.getText().trim().isEmpty()) ) return; //todo. Double.parseDouble(temp) throws an error if there is parenthesis present


		calculatorState.setLastOperator(actionCommand.charAt(0));

		calculatorState.getStringBuilder().append(calculatorUI.getOperandField().getText()).append(actionCommand);
		calculatorUI.getOperandField().setText("");
		calculatorUI.getResultField().setText(calculatorState.getStringBuilder().toString());
	}

	public void performSum() {
		if (calculatorState.getLastOperator() == null || calculatorState.getLastOperator() == '=') return;


		String operandFieldText = calculatorUI.getOperandField().getText();
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

	public void handleParenthesis(String actionCommand) {
		// if (actionCommand is ')' and there is no '(' in result field) return;
		String unsignedDouble = "(\\d+\\.?\\d*|\\.\\d+([Ee][-+]?\\d+)?)";

		calculatorState.setLastOperator(actionCommand.charAt(0));

		String tempOperand = calculatorUI.getOperandField().getText();
		String tempResult = calculatorUI.getResultField().getText();

		//  Condition 1:
		// If either the result field or operand field is not empty,
		// and the current operand is a valid double and not a closing parenthesis.
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

	private boolean isLastCharacterOperator(String resultToString) {
		if (isResultFieldEmpty())
			return false;
		char lastCharacterOfResult = resultToString.charAt(resultToString.length() - 1);
		return String.valueOf(lastCharacterOfResult).matches(InfixToPostfix.OPERATOR.toString());
	}
}
