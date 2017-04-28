package smartmetropolis.smartlab.model;

public enum SolicitationStatus {

	
	Finalizado("Finalizado"), Em_Progresso("Em progresso"), Aguardando("Aguardando verificação");

	private final String value;

	private SolicitationStatus(String s) {
		value = s;
	}

	public boolean equalsName(String otherName) {
		return (otherName == null) ? false : value.equals(otherName);
	}

	public String toString() {
		return this.value;
	}
}
