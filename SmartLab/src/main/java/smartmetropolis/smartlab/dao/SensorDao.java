package smartmetropolis.smartlab.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;

import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;
import br.ufrn.NGSI_10Client.entities.Attribute;
import br.ufrn.NGSI_10Client.entities.ContextElement;
import br.ufrn.NGSI_10Client.entities.Metadata;

public class SensorDao extends GenericDaoOrion<Sensor> implements
		SensorDaoInterface {

	private static final String type = "Sensor";
	
	private Logger logger = Logger.getLogger(SensorDao.class);

	private SimpleDateFormat format = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ssZ");

	public SensorDao() {
		super(type);
		format.setTimeZone(TimeZone.getTimeZone("GMT"));
	}

	public ContextElement entityToContextElement(Sensor entity) {

		ContextElement context = new ContextElement(type, entity.getId(), false);

		Attribute sala = new Attribute("Sala", "String", entity.getRoomName());
		context.addAttribute(sala);

		Attribute sensorType = new Attribute("Tipo_Sensor", "String", entity
				.getSensorType().toString());
		context.addAttribute(sensorType);

		Attribute descricao = new Attribute("Descricao", "String",
				entity.getDescription());
		context.addAttribute(descricao);

		String measurementValue = "null";
		String measurementTime = "null";

		if (entity.getMeasurement() != null
				&& entity.getMeasurement().getTime() != null) {

			measurementValue = entity.getMeasurement().getValue();

			measurementTime = format.format(entity.getMeasurement().getTime());
		}

		Attribute medicao = new Attribute("Medicao", "String", measurementValue);

		medicao.addMetadata(new Metadata("Data", "DateTime", measurementTime));

		context.addAttribute(medicao);

		return context;
	}

	public Sensor contextElementToEntity(ContextElement contextElement) {

		if (contextElement != null && contextElement.getAttributes() != null
				&& contextElement.getId() != null) {

			Sensor sensor = new Sensor();

			sensor.setId(contextElement.getId());
			sensor.setDescription(getAttributeValue("Descricao", contextElement));
			sensor.setRoomName(getAttributeValue("Sala", contextElement));

			String typeSensor = getAttributeValue("Tipo_Sensor", contextElement);

			if (typeSensor.equalsIgnoreCase(SensorType.HUMIDITY.toString())) {
				sensor.setSensorType(SensorType.HUMIDITY);
			} else if (typeSensor.equalsIgnoreCase(SensorType.OTHER.toString())) {
				sensor.setSensorType(SensorType.OTHER);
			} else if (typeSensor.equalsIgnoreCase(SensorType.PRESENCE
					.toString())) {
				sensor.setSensorType(SensorType.PRESENCE);
			} else if (typeSensor.equalsIgnoreCase(SensorType.TEMPERATURE
					.toString())) {
				sensor.setSensorType(SensorType.TEMPERATURE);
			}

			Measurement me = new Measurement();

			me.setSensorId(sensor.getId());
			me.setValue(getAttributeValue("Medicao", contextElement));
			try {

				String data = getAttributeMetadataValue("Medicao", "Data",
						contextElement);
				if (data != null && data != "null") {
					me.setTime(format.parse(data));
				}
			} catch (ParseException e) {
				logger.error("erro parsing data da medição: "+sensor);
			}

			sensor.setMeasurement(me);

			return sensor;
		}

		return null;
	}

	public static void main(String[] args) throws DAOException {

		SensorDaoInterface dao = new SensorDao();

		Sensor s = new Sensor();

		s.setDescription("sensor de presença sala B3018");
		s.setId("Sensor_de_presenca_n5");
		s.setRoomName("B318");
		s.setSensorType(SensorType.PRESENCE);

		Measurement m = new Measurement();
		m.setSensorId(s.getId());
		m.setTime(new Date());
		m.setValue("false");

		s.setMeasurement(m);

		dao.save(s);

		for (Sensor se : dao.findAll()) {
			System.out.println(se);
		}
	}

	public List<Sensor> findSensorByRoom(String rooomId) throws DAOException {

		List<Sensor> aux = null;

		List<Sensor> sensors = findAll();

		if (sensors != null && sensors.size() > 0) {

			aux = new ArrayList<Sensor>();

			for (Sensor s : sensors) {
				if (s.getRoomName().equalsIgnoreCase(rooomId)) {
					aux.add(s);
				}
			}

		}

		return aux;
	}

	public List<Sensor> findSensorByRoomAndType(String rooomId,
			SensorType sensorType) throws DAOException {

		List<Sensor> aux = null;

		List<Sensor> sensors = findSensorByRoom(rooomId);

		if (sensors != null && sensors.size() > 0) {

			aux = new ArrayList<Sensor>();

			for (Sensor s : sensors) {
				if (s.getSensorType().ordinal() == sensorType.ordinal()) {
					aux.add(s);
				}
			}

		}

		return aux;
	}
}
