package smartmetropolis.smartlab.model;

public enum GraphType {

	hora("1 Hora"), tres_horas("3 Horas"), seis_horas("6 Horas"), dia("1 Dia"), semana("1 Semana");

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
