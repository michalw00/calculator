public class CalculatorState {
	private StringBuilder stringBuilder;
	private Character lastOperator;
	private boolean errorState;

	public CalculatorState() {
		this.stringBuilder = new StringBuilder();
		this.errorState = false;
	}

	public void handleErrorState() {
		if (errorState) {
			CalculatorUI.operandField.setText("");
			errorState = false;
		}
	}




	public StringBuilder getStringBuilder() {
		return stringBuilder;
	}

	public Character getLastOperator() {
		return lastOperator;
	}

	public boolean isErrorState() {
		return errorState;
	}

	public void setStringBuilder(StringBuilder stringBuilder) {
		this.stringBuilder = stringBuilder;
	}

	public void setLastOperator(Character lastOperator) {
		this.lastOperator = lastOperator;
	}

	public void setErrorState(boolean errorState) {
		this.errorState = errorState;
	}
}
