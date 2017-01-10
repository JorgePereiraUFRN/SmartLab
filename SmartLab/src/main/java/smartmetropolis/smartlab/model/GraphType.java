package smartmetropolis.smartlab.model;

public enum GraphType {

	hora("Hora"), dia("Dia"), semana("Semana");

	private final String value;

	private GraphType(String s) {
		value = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : value.equals(otherName);
	}

	public String toString() {
		return this.value;
	}

}
