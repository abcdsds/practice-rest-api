package practicerestapi.events;

import org.springframework.hateoas.RepresentationModel;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.Link;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class EventResource extends RepresentationModel<EventResource> {

	@JsonUnwrapped
	private Event event;

	public EventResource(Event event) {
		this.event = event;
		add(linkTo(EventController.class).slash(this.event.getId()).withSelfRel());
	}
	
	public Event getEvent() {
		return event;
	}

}
