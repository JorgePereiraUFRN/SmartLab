package smartmetropolis.smartlab.model;

public enum SolicitationStatus {

	
	finalizado("Finalizado"), em_andamento("Em progresso"), aguardando("Aguardando verificação");

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
