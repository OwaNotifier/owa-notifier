package info.kapable.utils.owanotifier.service;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessageCollection {

	List<Message> value;

	public List<Message> getValue() {
		return value;
	}

	public void setValue(List<Message> value) {
		this.value = value;
	}
}
