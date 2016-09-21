package smartmetropolis.smartlab.model;

public enum AirConditionerAction {

	desligar("desligar ar-condicionado"), ligar("acionar ar-condicionado"), aumentar_temperatura(
			"aumentar temperatura"), diminuir_temperatura(
			"diminuir temperatura");

	private final String value;

	private AirConditionerAction(String s) {
		value = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : value.equals(otherName);
	}

	public String toString() {
		return this.value;
	}

}
