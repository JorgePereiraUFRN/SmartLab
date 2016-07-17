package smartmetropolis.smartlab.model;

import java.io.Serializable;

import javax.persistence.Embeddable;



public class RoomKey implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String localName;
	private String roomName;
	
	
	public boolean equals(Object obj) {
		if (obj instanceof RoomKey) {

			RoomKey key = (RoomKey) obj;

			if (!key.getLocalName().equals(localName)) {
				return false;
			}

			if (!key.getRoomName().equals(roomName)) {
				return false;
			}

			return true;
		}

		return false;
	}

	public int hashCode() {
		return localName.hashCode() + roomName.hashCode();
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

}
