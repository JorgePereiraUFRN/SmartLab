package smartmetropolis.smartlab.model;



public class Room {

	private String roomName;
	private String predio;


	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public String getPredio() {
		return predio;
	}

	public void setPredio(String predio) {
		this.predio = predio;
	}

	@Override
	public String toString() {
		return "Room [roomName=" + roomName + ", predio=" + predio + "]";
	}


}