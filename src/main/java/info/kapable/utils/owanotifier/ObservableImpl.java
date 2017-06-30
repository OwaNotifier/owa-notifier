package info.kapable.utils.owanotifier;

import java.util.Observable;

public class ObservableImpl extends Observable {

	public void update() {
		this.setChanged();
	}
}
