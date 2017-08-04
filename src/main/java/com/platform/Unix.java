package com.platform;

public class Unix implements OperatingSystem {
	@Override
	public boolean isSupported(String feature) {
		if(feature.contentEquals("fade")) {
			return false;
		} else {
			return true;
		}
	}
}
