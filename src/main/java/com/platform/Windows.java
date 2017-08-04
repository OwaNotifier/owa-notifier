package com.platform;

public class Windows implements OperatingSystem {
	@Override
	public boolean isSupported(String feature) {
		if(feature.contentEquals("fade")) {
			return false;
		} else {
			return true;
		}
	}
}
