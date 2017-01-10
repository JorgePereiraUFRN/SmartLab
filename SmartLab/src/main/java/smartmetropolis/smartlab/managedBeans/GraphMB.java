package smartmetropolis.smartlab.managedBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;

import smartmetropolis.smartlab.controller.LocalController;
import smartmetropolis.smartlab.controller.MeasurementController;
import smartmetropolis.smartlab.controller.RoomController;
import smartmetropolis.smartlab.controller.SensorController;
import smartmetropolis.smartlab.exceptions.DAOException;
import smartmetropolis.smartlab.exceptions.validateDataException;
import smartmetropolis.smartlab.model.GraphType;
import smartmetropolis.smartlab.model.Local;
import smartmetropolis.smartlab.model.Measurement;
import smartmetropolis.smartlab.model.Room;
import smartmetropolis.smartlab.model.Sensor;
import smartmetropolis.smartlab.model.SensorType;

@ManagedBean(name = "graphMB")
@SessionScoped
public class GraphMB {

	private String localName;
	private String roomName;
	private String graphType;

	private boolean temperatura, umidade, presenca;

	private int graphSize = 900;
	private String ghrapStyle = "width:900px;";
	private LineChartModel lineModel;
	private Date initialDate;
	private Date finalDate;
	SimpleDateFormat df = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");

	private Map<String, String> localsMap;
	private Map<String, String> roomsMap;
	private Map<String, String> graphTypes;

	private LocalController localController;
	private RoomController roomController;
	private MeasurementController measurementController;

	public GraphMB() {

		localController = LocalController.getInstance();
		roomController = RoomController.getInstance();
		measurementController = MeasurementController.getInstance();

		lineModel = new LineChartModel();

		initLocalsMap();

		graphTypes = new HashMap<String, String>();
		graphTypes.put(GraphType.hora.toString(), GraphType.hora.toString());
		graphTypes.put(GraphType.dia.toString(), GraphType.dia.toString());
		graphTypes
				.put(GraphType.semana.toString(), GraphType.semana.toString());
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

	public void selectedRoom() {
		System.out.println(roomName);
	}

	public void selectedDate() {
		System.out.println(initialDate);
	}

	public void selectedGraphType() {
		System.out.println(graphType);
	}

	public void selectedSensor() {
		System.out.println("temperature: " + temperatura + ", preseça: "
				+ presenca + ", umidade: " + umidade);
	}

	private ChartSeries getMeasurements(String roomName, SensorType sensorType)
			throws Exception {
		try {
			Room room = roomController.findRoom(roomName, localName);

			if (room == null || room.getSensors() == null) {
				throw new Exception("Room: " + roomName + " não encontrada");
			}

			Sensor sensor = null;

			for (Sensor s : room.getSensors()) {
				if (s.getSensorType().ordinal() == sensorType.ordinal()) {
					sensor = s;
					break;
				}
			}

			if (sensor == null) {
				throw new Exception("Sensor " + sensorType.toString()
						+ " inexistente");
			}


			
			Calendar c = Calendar.getInstance();

			if (initialDate == null) {
				throw new Exception("Expecifique a data inicial");
			}

			c.setTime(initialDate);

			if (graphType.equalsIgnoreCase(GraphType.hora.toString())) {
				c.add(Calendar.HOUR, 1);
			} else if (graphType.equalsIgnoreCase(GraphType.dia.toString())) {
				c.add(Calendar.DAY_OF_MONTH, 1);
			} else if (graphType.equalsIgnoreCase(GraphType.semana.toString())) {
				c.add(Calendar.DAY_OF_MONTH, 7);
			}

			finalDate = c.getTime();
		

			List<Measurement> measurements = measurementController
					.listMeasurementsBySensorAndDate(initialDate, finalDate,
							sensor.getId());

			ChartSeries series = new ChartSeries();
			series.setLabel(sensorType.toString());

			Measurement[] measurementsArray = measurements
					.toArray(new Measurement[measurements.size()]);

			Arrays.sort(measurementsArray);

			int day = 0;
			for (int i = 0; i < measurementsArray.length; i++) {

				Float value = null;
				Measurement m = measurementsArray[i];

				if (sensorType.ordinal() == SensorType.PRESENCE.ordinal()) {

					if (Boolean.parseBoolean(m.getValue())) {
						value = 20F;
					} else {
						value = 0F;
					}

				} else {
					value = Float.parseFloat(m.getValue());
				}

				
				SimpleDateFormat df2 = new SimpleDateFormat("HH:mm:ss");

				// System.out.println(df2.format(m.getTime()));
				// if (day != m.getTime().getDay()) {
				series.set(df.format(m.getTime()), value);
				day = m.getTime().getDay();
				// } else {

				// series.set(df2.format(m.getTime()), value);
				// }

			}

			if (measurements.size() * 20 > graphSize) {
				graphSize = measurements.size() * 20;
			}

			System.out.println(initialDate + " " + finalDate + "  "
					+ measurements.size());

			series.setLabel(sensorType.toString());
			return series;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao gerar gráfico. " + e.getMessage());
		}

	}

	public void generateGraph() throws NumberFormatException, DAOException {

		try {
			lineModel = new LineChartModel();

			lineModel.getAxes().put(AxisType.X, new CategoryAxis("data"));

			Axis yAxis = lineModel.getAxis(AxisType.Y);
			Axis xAxis = lineModel.getAxis(AxisType.X);
			xAxis.setTickAngle(45);

			yAxis.setLabel("valores");

			yAxis.setMin(0);
			yAxis.setMax(35);

			DateAxis axis = new DateAxis("Dates");
			axis.setTickAngle(-50);

			//axis.setMin(df.format(initialDate));
			axis.setMax("2017-01-06");
			axis.setTickFormat("%b %#d, %y %H:%#M:%S");

			lineModel.getAxes().put(AxisType.X, axis);

			if (umidade) {
				yAxis.setMin(15);
				yAxis.setMax(100);
			}

			lineModel.setTitle(SensorType.HUMIDITY.toString());

			if (presenca) {
				lineModel.addSeries(getMeasurements(roomName,
						SensorType.PRESENCE));
			}
			if (temperatura) {
				lineModel.addSeries(getMeasurements(roomName,
						SensorType.TEMPERATURE));
			}
			if (umidade) {
				lineModel.addSeries(getMeasurements(roomName,
						SensorType.HUMIDITY));
			}

			setGhrapStyle("width:" + graphSize + "px;height:400px");

		} catch (Exception e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", e
							.getMessage()));
		}
	}

	public Date getInitialDate() {
		return initialDate;
	}

	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
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

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getRoomName() {
		return roomName;
	}

	public boolean isTemperatura() {
		return temperatura;
	}

	public void setTemperatura(boolean temperatura) {
		this.temperatura = temperatura;
	}

	public boolean isUmidade() {
		return umidade;
	}

	public void setUmidade(boolean umidade) {
		this.umidade = umidade;
	}

	public boolean isPresenca() {
		return presenca;
	}

	public void setPresenca(boolean presenca) {
		this.presenca = presenca;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public Map<String, String> getLocalsMap() {
		return localsMap;
	}

	public void setLocalsMap(Map<String, String> localsMap) {
		this.localsMap = localsMap;
	}

	public String getGraphType() {
		return graphType;
	}

	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}

	public Map<String, String> getGraphTypes() {
		return graphTypes;
	}

	public void setGraphTypes(Map<String, String> graphTypes) {
		this.graphTypes = graphTypes;
	}

	public Map<String, String> getRoomsMap() {
		return roomsMap;
	}

	public void setRoomsMap(Map<String, String> roomsMap) {
		this.roomsMap = roomsMap;
	}

}
