package smartmetropolis.smartlab.model;

public enum SensorType {
	
	TEMPERATURE("Sensor de Temperatura"), HUMIDITY ("Sesnor de Umidade"), PRESENCE ("Sensor de Presença"), OTHER ("Outro"); 

	private final String value;

	private SensorType(String s) {
		value = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : value.equals(otherName);
	}

	public String toString() {
		return this.value;
	}
}
