import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ActionListeners {




	public static class NumberListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			try {
				Calculator.numbers(e);
			}
			catch (NumberFormatException e2) {
				Calculator.operandField.setText("Error: Reenter Number.");
			}
		}
	}
	public static class OperationListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Calculator.operations(e);
		}
	}
	public static class SumListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Calculator.sum();
		}
	}
	public static class ParenthesisListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			Calculator.addParenthesis(e);
		}
	}




}
