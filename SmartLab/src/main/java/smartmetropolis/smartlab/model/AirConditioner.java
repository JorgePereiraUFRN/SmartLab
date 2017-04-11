package smartmetropolis.smartlab.model;


public class AirConditioner {

	private String id;
	private String roomId;
	private String ipaddressAirControl;

	public AirConditioner() {
		// TODO Auto-generated constructor stub
	}

	public Boolean itsOn;

	public Boolean getItsOn() {
		return itsOn;
	}

	public void setItsOn(Boolean itsOn) {
		this.itsOn = itsOn;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIpaddressAirControl() {
		return ipaddressAirControl;
	}

	public void setIpaddressAirControl(String ipaddressAirControl) {
		this.ipaddressAirControl = ipaddressAirControl;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	@Override
	public String toString() {
		return "AirConditioner [id=" + id + ", rommId=" + roomId
				+ ", ipaddressAirControl=" + ipaddressAirControl + ", itsOn="
				+ itsOn + "]";
	}

}
