package smartmetropolis.smartlab.model;


public class AirConditioner {

	private String id;
	private String roomId;
	private String ipAddressAirControl;

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

	public String getIpAddressAirControl() {
		return ipAddressAirControl;
	}

	public void setIpAddressAirControl(String ipaddressAirControl) {
		this.ipAddressAirControl = ipaddressAirControl;
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
				+ ", ipaddressAirControl=" + ipAddressAirControl + ", itsOn="
				+ itsOn + "]";
	}

}
