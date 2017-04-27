package smartmetropolis.smartlab.model;

public enum ResourceType {

	Compututer("Computador"), Sensor("Sensor"), AirConditioner("Ar Condicionado"), Software("Software"), Other("Outro Recurso");

	private final String value;

	private ResourceType(String s) {
		value = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : value.equals(otherName);
	}

	public String toString() {
		return this.value;
	}
	
}
