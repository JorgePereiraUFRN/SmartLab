package smartmetropolis.smartlab.managedBeans;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Sensor;

@ManagedBean(name = "SensorService", eager = true)
@ApplicationScoped
public class SensorService {

	private List<Sensor> sensors;
	
	public SensorService(){
		init();
	}

	@PostConstruct
	public void init() {

		SensorController controller = SensorController.getInstance();

		try {
			sensors = controller.getInstance().findSensors();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<Sensor> getSensors() {
		return sensors;
	}

	public void setSensors(List<Sensor> sensors) {
		this.sensors = sensors;
	}
	
	

}
