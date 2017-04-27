package smartmetropolis.smartlab.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Resource {
	
	@Id
	@GeneratedValue
	private Long id;
	private String room;
	private ResourceType resourceType;
	private String resourceName;
	private String descrition;
	private Date instalationDate;

	public Resource() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public ResourceType getResourceType() {
		return resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getDescrition() {
		return descrition;
	}

	public void setDescrition(String descrition) {
		this.descrition = descrition;
	}

	public Date getInstalationDate() {
		return instalationDate;
	}

	public void setInstalationDate(Date instalationDate) {
		this.instalationDate = instalationDate;
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", room=" + room + ", resourceType="
				+ resourceType + ", resourceName=" + resourceName
				+ ", descrition=" + descrition + ", instalationDate="
				+ instalationDate + "]";
	}

	
	
}
