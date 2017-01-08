package smartmetropolis.smartlab.managedBeans;

import java.nio.channels.UnsupportedAddressTypeException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

@ManagedBean(name = "measurementB")
@SessionScoped
public class MeasurementMB {

	private String localName;
	private String roomName;
	private String sensorId;
	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;
	private Map<String, String> sensorsMap;
	private LocalController localController;
	private List<Measurement> measurements;

	private LineChartModel lineModel;

	private SensorController sensorController;
	private RoomController roomController;
	private MeasurementController measurementController;

	private Date initialDate;
	private Date finalDate;
	
	private String ghrapStyle = "width:900px;";

	public MeasurementMB() {
		sensorController = SensorController.getInstance();
		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();
		measurementController = MeasurementController.getInstance();
		measurements = new ArrayList<Measurement>();
		lineModel = new LineChartModel();

		initLocalsMap();
	}

	private void initLocalsMap() {

		try {
			List<Local> locals = localController.findAllLocals();
			localsMap = new HashMap<String, String>();

			for (Local l : locals) {
				localsMap.put(l.getLocalName(), l.getLocalName());
			}
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}

	}

	public void initRoomsMap() {

		roomsMap = new HashMap<String, String>();

		try {
			Local l = localController.findLocal(localName);

			if (l != null) {
				for (Room r : l.getRooms()) {
					roomsMap.put(r.getRoomName(), r.getRoomName());
				}
			}

		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}
	}

	public void initSensorsMap() {

		sensorsMap = new HashMap<String, String>();

		try {
			Room r = roomController.findRoom(roomName, localName);
			if (r != null) {

				for (Sensor s : r.getSensors()) {
					sensorsMap.put(s.getSensorType() + " - Id: " + s.getId(), s
							.getId().toString());
				}
			}

		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		} catch (validateDataException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Dados inválidos: ", e.getMessage()));
		}
	}

	public void listMeasurements() {

		measurements = null;
		try {

			if ((sensorId != null || !sensorId.equals(""))
					&& initialDate != null && finalDate != null) {
				long id = Long.parseLong(sensorId);

				measurements = measurementController
						.listMeasurementsBySensorAndDate(initialDate,
								finalDate, id);

			}
		} catch (DAOException e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Erro ao recuperar dados: ", e.getMessage()));
		}

	}

	public void generateGraph() throws NumberFormatException, DAOException {

		try {
			lineModel = new LineChartModel();

			if (measurements == null || measurements.size() == 0) {
				throw new validateDataException(
						"Especifique os critérios de busca!");

			}

			//lineModel.setShowPointLabels(true);
			lineModel.getAxes().put(AxisType.X, new CategoryAxis("data"));

			Axis yAxis = lineModel.getAxis(AxisType.Y);
			Axis xAxis = lineModel.getAxis(AxisType.X);
			xAxis.setTickAngle(45);
			
			yAxis.setLabel("valores");

			Sensor s = sensorController.findSensor(Long.parseLong(sensorId));

			if (s.getSensorType().ordinal() == SensorType.OTHER.ordinal()) {

				FacesContext.getCurrentInstance().addMessage(
						null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								"Erro ao gerar grafico ",
								"imposivel gerar graficos para esse sensor."));
				return;
			}

			if (s.getSensorType().ordinal() == SensorType.HUMIDITY.ordinal()) {
				lineModel.setTitle(SensorType.HUMIDITY.toString());
				yAxis.setMin(0);
				yAxis.setMax(100);
			} else if (s.getSensorType().ordinal() == SensorType.TEMPERATURE
					.ordinal()) {
				lineModel.setTitle(SensorType.TEMPERATURE.toString());
				yAxis.setMin(0);
				yAxis.setMax(35);
			}
			else if (s.getSensorType().ordinal() == SensorType.PRESENCE
					.ordinal()) {
				lineModel.setTitle(SensorType.PRESENCE.toString());
				yAxis.setMin(0);
				yAxis.setMax(2);
			}

			ChartSeries series = new ChartSeries();

			Measurement[] measurementsArray = measurements
					.toArray(new Measurement[measurements.size()]);

			Arrays.sort(measurementsArray);

			int day = 0;
			for (int i = 0; i < measurementsArray.length; i++) {

				Float value = null;
				Measurement m = measurementsArray[i];

				if(s.getSensorType().ordinal() == s.getSensorType().PRESENCE.ordinal()){
					
					if(Boolean.parseBoolean(m.getValue())){
						value = 1F;
					}else{
						value = 0F;
					}
					
				}else{
				 value = Float.parseFloat(m.getValue());
				}

				SimpleDateFormat df1 = new SimpleDateFormat("[dd-MM] HH:mm");
				SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");

				if (day != m.getTime().getDay()) {
					series.set(df1.format(m.getTime()), value);
					day = m.getTime().getDay();
				} else {

					series.set(df2.format(m.getTime()), value);
				}

			}

			setGhrapStyle("width:"+measurementsArray.length * 20 +"px;height:400px");
			lineModel.addSeries(series);

			// lineModel.setExtender("extender");

		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", e
							.getMessage()));
		}
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getSensorId() {
		return sensorId;
	}

	public void setSensorId(String sensorId) {
		this.sensorId = sensorId;
	}

	public Map<String, String> getLocalsMap() {
		return localsMap;
	}

	public void setLocalsMap(Map<String, String> localsMap) {
		this.localsMap = localsMap;
	}

	public Map<String, String> getRoomsMap() {
		return roomsMap;
	}

	public void setRoomsMap(Map<String, String> roomsMap) {
		this.roomsMap = roomsMap;
	}

	public Map<String, String> getSensorsMap() {
		return sensorsMap;
	}

	public void setSensorsMap(Map<String, String> sensorsMap) {
		this.sensorsMap = sensorsMap;
	}

	public List<Measurement> getMeasurements() {
		return measurements;
	}

	public void setMeasurements(List<Measurement> measurements) {
		this.measurements = measurements;
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	public Date getFinalDate() {
		return finalDate;
	}

	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	public LineChartModel getLineModel() {
		return lineModel;
	}

	public void setLineModel(LineChartModel lineModel) {
		this.lineModel = lineModel;
	}

	public String getGhrapStyle() {
		return ghrapStyle;
	}

	public void setGhrapStyle(String ghrapStyle) {
		this.ghrapStyle = ghrapStyle;
	}
	
	
}
