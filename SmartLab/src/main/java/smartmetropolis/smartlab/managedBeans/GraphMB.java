package smartmetropolis.smartlab.managedBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LegendPlacement;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

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
	
	private String graphTiTle;

	private String localName;
	private String roomName;
	private String graphType;
	
	private String label1, label2, label3; 

	private boolean temperatura, umidade, presenca;

	private int graphSize = 900;
	private String ghrapStyle = "width:900px;";
	private LineChartModel lineModel;
	private Date initialDate;
	private Date grahfinalDate, graphInitDate;
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

		initLocalsMap();

		graphTypes = new HashMap<String, String>();
		graphTypes.put(GraphType.hora.toString(), GraphType.hora.toString());
		graphTypes.put(GraphType.tres_horas.toString(),
				GraphType.tres_horas.toString());
		graphTypes.put(GraphType.seis_horas.toString(),
				GraphType.seis_horas.toString());
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
				for (Room r : roomController.findRoomsByBuilding(l.getLocalName())) {
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

	private List<Measurement> avgTempHumidMeasurements(
			List<Measurement> measurements, int time) {

		List<Measurement> processedMeasurements = new ArrayList<Measurement>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -100);
		Date oldDateAdded = calendar.getTime();

		int cont = 0;
		int sum = 0;

		for (Measurement m : measurements) {

			if (m.getSensor().getSensorType().ordinal() == SensorType.TEMPERATURE
					.ordinal()
					|| m.getSensor().getSensorType().ordinal() == SensorType.HUMIDITY
							.ordinal()) {

				long diferrence = m.getTime().getTime()
						- oldDateAdded.getTime();

				long seconds = TimeUnit.MILLISECONDS.toSeconds(diferrence);

				int value = Integer.parseInt(m.getValue());

				sum += value;
				cont++;

				if (seconds > time) {

					Measurement me = new Measurement();

					int avg = sum / cont;

					me.setValue(String.valueOf(avg));
					me.setTime(m.getTime());

					processedMeasurements.add(me);
					

					cont = 0;
					sum = 0;

					oldDateAdded = m.getTime();
				}

			}
		}

		return processedMeasurements;

	}

	private List<Measurement> avgPresenceMeasurements(
			List<Measurement> measurements, int time) {

		List<Measurement> processedMeasurements = new ArrayList<Measurement>();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -100);
		Date oldDateAdded = calendar.getTime();

		Measurement lastAddedMeasurement = null;

		Measurement previus = null;

		for (Measurement m : measurements) {

			if (m.getSensor().getSensorType().ordinal() == SensorType.PRESENCE
					.ordinal()) {

				if (previus != null) {

					long diferrence = m.getTime().getTime()
							- previus.getTime().getTime();
					long seconds = TimeUnit.MILLISECONDS.toSeconds(diferrence);

					if (seconds > time) {

						Measurement m1 = new Measurement();
						m1.setValue("false");
						m1.setTime(new Date(m.getTime().getTime()
								- (5 * 1000 * 60)));
						processedMeasurements.add(m1);

						Measurement m2 = new Measurement();
						m2.setValue("false");
						m2.setTime(new Date(previus.getTime().getTime()
								+ (1000 * 60)));
						processedMeasurements.add(m2);

					}
				}

				long diferrence = m.getTime().getTime()
						- oldDateAdded.getTime();

				long seconds = TimeUnit.MILLISECONDS.toSeconds(diferrence);

				if (seconds > time) {
					lastAddedMeasurement = m;
					processedMeasurements.add(lastAddedMeasurement);
					oldDateAdded = m.getTime();
				} else {

					boolean last = lastAddedMeasurement.getValue()
							.equalsIgnoreCase("true");
					boolean atual = m.getValue().equalsIgnoreCase("true");

					if (!last && atual) {
						lastAddedMeasurement.setValue("true");
					}
				}

			}

			previus = m;
		}

		return processedMeasurements;
	}

	private LineChartSeries getMeasurements(String roomName, SensorType sensorType)
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

			Date finalDate = null;

			Calendar c = Calendar.getInstance();

			if (initialDate == null) {
				throw new Exception("Expecifique a data inicial");
			}

			c.setTime(initialDate);

			if (graphType.equalsIgnoreCase(GraphType.hora.toString())) {
				c.add(Calendar.HOUR, 1);
			} else if (graphType.equalsIgnoreCase(GraphType.tres_horas
					.toString())) {
				c.add(Calendar.HOUR, 3);
			} else if (graphType.equalsIgnoreCase(GraphType.seis_horas
					.toString())) {
				c.add(Calendar.HOUR, 6);
			} else if (graphType.equalsIgnoreCase(GraphType.dia.toString())) {
				c.add(Calendar.DAY_OF_MONTH, 1);
			} else if (graphType.equalsIgnoreCase(GraphType.semana.toString())) {
				c.add(Calendar.DAY_OF_MONTH, 7);
			}

			finalDate = c.getTime();

			List<Measurement> measurements = measurementController
					.listMeasurementsBySensorAndDate(initialDate, finalDate,
							sensor.getId());

			if (measurements == null || measurements.size() == 0) {
				return null;
			}

			if (graphType.equalsIgnoreCase(GraphType.semana.toString())) {

				if (sensorType.ordinal() == SensorType.PRESENCE.ordinal()) {
					measurements = avgPresenceMeasurements(measurements,
							20 * 60);
				} else {
					measurements = avgTempHumidMeasurements(measurements,
							20 * 60);
				}

			} else {
				if (sensorType.ordinal() == SensorType.PRESENCE.ordinal()) {
					measurements = avgPresenceMeasurements(measurements, 5 * 60);
				}
			}

			LineChartSeries series = new LineChartSeries();


			Measurement[] measurementsArray = measurements
					.toArray(new Measurement[measurements.size()]);

			Arrays.sort(measurementsArray);

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

				if (graphInitDate == null
						|| m.getTime().getTime() < graphInitDate.getTime()) {
					graphInitDate = m.getTime();
				}

				if (grahfinalDate == null
						|| m.getTime().getTime() > grahfinalDate.getTime()) {
					grahfinalDate = m.getTime();
				}

				series.set(df.format(m.getTime()), value);
			}

			if (measurements.size() * 20 > graphSize) {
				graphSize = measurements.size() * 20;
			}

			

			return series;

		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("Erro ao gerar gráfico. " + e.getMessage());
		}

	}

	public void generateGraph() throws NumberFormatException, DAOException {

		graphSize = 900;
		grahfinalDate = null;
		graphInitDate = null;

		try {

			lineModel = new LineChartModel();

			
			

			lineModel.getAxes().put(AxisType.X, new CategoryAxis("data"));

			Axis yAxis = lineModel.getAxis(AxisType.Y);

			yAxis.setLabel("valores");

			yAxis.setMin(0);
			yAxis.setMax(35);

			List<LineChartSeries> series = new ArrayList<LineChartSeries>();

			label1 = label2 = label3 = "";
			String seriesColors = "";
			
			if (presenca) {
				LineChartSeries serie = getMeasurements(roomName, SensorType.PRESENCE);
				if (serie != null) {
					
					series.add(serie);
					label1 = "presença";
					seriesColors+="58BA27,";
				}
			}
			if (temperatura) {
				LineChartSeries serie = getMeasurements(roomName, SensorType.TEMPERATURE);
				if (serie != null) {
					
					series.add(serie);
					label2 = "temperatura";
					seriesColors+="FFCC33,";
				}
			}
			if (umidade) {
				LineChartSeries serie = getMeasurements(roomName, SensorType.HUMIDITY);
				if (serie != null) {
				
					series.add(serie);
					label3 = "umidade";
					
					seriesColors+="F74A4A";
				}
			}

			DateAxis axis = new DateAxis("Date");
			axis.setTickAngle(-50);

			axis.setMin(df.format(graphInitDate));
			axis.setMax(df.format(grahfinalDate));

			axis.setTickFormat("%b %#d, %H:%#M:%S");
			lineModel.getAxes().put(AxisType.X, axis);
			
			lineModel.setZoom(true);

			if (umidade) {
				yAxis.setMin(15);
				yAxis.setMax(100);
			}
			
			graphTiTle = "Dados de: ";

			if(presenca){
				graphTiTle+=" Presença";
			}if(temperatura){
				graphTiTle+=" - Temperatura";
			}if(umidade){
				graphTiTle+=" - Umidade";
			}
			
			lineModel.setTitle(graphTiTle);

			for (ChartSeries serie : series) {
				lineModel.addSeries(serie);
			
			}
			
			lineModel.setSeriesColors(seriesColors);
			
			

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

	public String getLabel1() {
		return label1;
	}

	public void setLabel1(String label1) {
		this.label1 = label1;
	}

	public String getLabel2() {
		return label2;
	}

	public void setLabel2(String label2) {
		this.label2 = label2;
	}

	public String getLabel3() {
		return label3;
	}

	public void setLabel3(String label3) {
		this.label3 = label3;
	}
	
	

}
