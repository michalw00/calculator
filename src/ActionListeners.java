import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListeners {
	private static CalculatorModel calculatorModel;

	public ActionListeners(CalculatorModel calculatorModel) {
		ActionListeners.calculatorModel = calculatorModel;
	}

	public static class NumberListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			calculatorModel.handleNumberInput(e);
		}

	}

	public static class OperationListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			String actionCommand = e.getActionCommand();

			if (actionCommand.matches("[+\\-*/]") ) {
				calculatorModel.handleOperationInput(actionCommand);
				return;
			}

			if (actionCommand.charAt(0) == '(' || actionCommand.charAt(0) == ')') {
				calculatorModel.handleParenthesis(actionCommand);
				return;
			}

			if (actionCommand.charAt(0) == '=' ) {
				calculatorModel.performSum();
				return;
			}

			if (actionCommand.charAt(0) == 'E' ) {
				calculatorModel.plotGraph();
			}

		}

	}

	public static class TrigonometryListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// todo
		}

	}


}
