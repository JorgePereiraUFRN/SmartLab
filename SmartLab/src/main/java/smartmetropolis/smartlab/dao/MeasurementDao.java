package smartmetropolis.smartlab.dao;

import java.util.Date;
import java.util.Calendar;
import java.util.List;

import javafx.scene.chart.PieChart.Data;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.RoomKey;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

public class MeasurementDao extends GenericHibernateDAO<Measurement, Long>
		implements MeasurementDaoInterface {

	public List<Measurement> listMeasurementsByDate(Date date)
			throws DAOException {

		List<Measurement> list = null;
		try {
			list = getInstance()
					.createQuery(
							"select m from "
									+ Measurement.class.getSimpleName()
									+ " m, " + Sensor.class.getSimpleName()
									+ " s, " + Room.class.getSimpleName()
									+ " r where  m.time > :time")
					.setParameter("time", date).getResultList();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return list;
	}

	public List<Measurement> listMeasurementsBetweendDate(Date initialDate,
			Date finalDate, Long sensorId) throws DAOException {
		List<Measurement> list = null;
		try {
			list = getInstance()
					.createQuery(
							"select m from "
									+ Measurement.class.getSimpleName()
									/*+ " as m Inner Join "
									+ Sensor.class.getSimpleName()
									+ " as s "*/
									+ " m where m.time between :initialDate AND :finalDate")
					.setParameter("initialDate", initialDate)
					.setParameter("finalDate", finalDate).getResultList();
		} catch (Exception e) {
			throw new DAOException(e.getMessage());
		}
		return list;
	}

	/*
	 * public static void main(String ags[]) throws DAOException {
	 * 
	 * MeasurementDaoInterface measurementDao = new MeasurementDao();
	 * 
	 * Measurement m = measurementDao.findById(Measurement.class, 2l);
	 * 
	 * Calendar calendar = Calendar.getInstance();
	 * calendar.setTimeInMillis(System.currentTimeMillis());
	 * calendar.add(Calendar.MINUTE, -500);
	 * 
	 * 
	 * List<Measurement> list = measurementDao.listMeasurementsByDate(
	 * calendar.getTime());
	 * 
	 * for (Measurement me : list) { System.out.println(me); } }
	 */
}
