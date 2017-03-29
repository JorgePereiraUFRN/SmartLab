package smartmetropolis.smartlab.model;

import java.sql.Time;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlRootElement()
public class Reserva {
	
	private String justificativa;
	private String titulo;
	private Date data;
	private Time horaInicio;
	private Time horaFim;
	private String codigoSala;
	private String tipoSala;
	private String resposavel;
	private String solicitante;
	private String situacao;


	public String getJustificativa() {
		return justificativa;
	}

	public void setJustificativa(String justificativa) {
		this.justificativa = justificativa;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Time getHoraInicio() {
		return horaInicio;
	}

	public void setHoraInicio(Time horaInicio) {
		this.horaInicio = horaInicio;
	}

	public Time getHoraFim() {
		return horaFim;
	}

	public void setHoraFim(Time horaFim) {
		this.horaFim = horaFim;
	}

	public String getCodigoSala() {
		return codigoSala;
	}

	public void setCodigoSala(String codigoSala) {
		this.codigoSala = codigoSala;
	}

	public String getTipoSala() {
		return tipoSala;
	}

	public void setTipoSala(String tipoSala) {
		this.tipoSala = tipoSala;
	}

	public String getResposavel() {
		return resposavel;
	}

	public void setResposavel(String resposavel) {
		this.resposavel = resposavel;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@Override
	public String toString() {
		return "Reserva [justificativa=" + justificativa + ", titulo=" + titulo
				+ ", data=" + data.toGMTString() + ", horaInicio=" + horaInicio
				+ ", horaFim=" + horaFim + ", codigoSala=" + codigoSala
				+ ", tipoSala=" + tipoSala + ", resposavel=" + resposavel
				+ ", solicitante=" + solicitante + ", situacao=" + situacao
				+ "]";
	}

	
}
