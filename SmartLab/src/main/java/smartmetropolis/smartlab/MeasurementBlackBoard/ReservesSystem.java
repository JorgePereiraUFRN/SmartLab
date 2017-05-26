package smartmetropolis.smartlab.MeasurementBlackBoard;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.model.Reserva;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class ReservesSystem {
	
	private Logger logger = Logger.getLogger(ReservesSystem.class);

	public boolean hasApprovedReserves(String roomCode, int time) {

		Date currentDate = new Date();

		List<Reserva> reservas = getReserves(roomCode);
		
		if(reservas == null){
			return false;
		}
		
		for (Reserva r : reservas) {

			if (r.getSituacao().equalsIgnoreCase("2 - Aprovada")
					&& r.getData() != null) {

				long duration = r.getData().getTime() - currentDate.getTime();

				long timeMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);

				if (timeMinutes < time) {
	
					return true;
				}
			}
		}

		return false;
	}

	private List<Reserva> getReserves(String roomCode) {

		try {
			ClientConfig clientConfig = new DefaultClientConfig();
			clientConfig.getFeatures().put(
					JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
			Client client = Client.create(clientConfig);
			logger.debug("enviando requisição: "+"https://testes.imd.ufrn.br/keys/services/reserva/sala/"
							+ roomCode);
			List<Reserva> reservas = client.resource(
					"https://testes.imd.ufrn.br/keys/services/reserva/sala/"
							+ roomCode).get(new GenericType<List<Reserva>>() {
			});

			return reservas;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

}
