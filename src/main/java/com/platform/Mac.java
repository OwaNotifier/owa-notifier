package com.platform;

public class Mac implements OperatingSystem {
	@Override
	public boolean isSupported(String feature) {
		if(feature.contentEquals("fade")) {
			return true;
		} else {
			return true;
		}
	}

}
