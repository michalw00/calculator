import java.awt.*;

public class CalculatorState {
	private Calculator calculator;
	private StringBuilder stringBuilder;
	private Character lastOperator;
	private boolean errorState;
	private Graphics graphicsState;

	public CalculatorState(Calculator calculator) {
		this.calculator = calculator;
		this.stringBuilder = new StringBuilder();
		this.errorState = false;
	}

	public void handleErrorState() {
		if (errorState) {
			calculator.getCalculatorUI().getOperandField().setText("");
			errorState = false;
		}
	}




	//---boilerplate stuff---

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

	public Graphics getGraphicsState() {
		return graphicsState;
	}

	public void setGraphicsState(Graphics graphicsState) {
		this.graphicsState = graphicsState;
	}
}
