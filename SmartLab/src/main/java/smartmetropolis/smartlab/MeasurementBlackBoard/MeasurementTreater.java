package smartmetropolis.smartlab.MeasurementBlackBoard;

import smartmetropolis.smartlab.exceptions.TreaterException;
import smartmetropolis.smartlab.model.Measurement;

public abstract class MeasurementTreater{

	protected MeasurementTreater next = null;
	
	public void setNext(MeasurementTreater measurementTreater){
		if(this.next == null){
			this.next = measurementTreater;

		}else{
			this.next.setNext(measurementTreater);
		}
	}
	
	public abstract void treaterMeasurement(Measurement measurement) throws TreaterException;

}
