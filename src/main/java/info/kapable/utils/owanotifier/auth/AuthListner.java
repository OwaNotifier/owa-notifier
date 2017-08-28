package info.kapable.utils.owanotifier.auth;

import java.util.Observable;

public abstract class AuthListner extends Observable {

	public TokenResponse tokenResponse;
	public IdToken idTokenObj;
}
