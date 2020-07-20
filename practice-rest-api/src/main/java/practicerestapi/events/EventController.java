package practicerestapi.events;

import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping(value = "/api/events" , produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody EventDto eventDto) {
		//URI uri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
		Event saveEvent = eventRepository.save(modelMapper.map(eventDto, Event.class));
		
		URI uri = linkTo(EventController.class).slash(saveEvent.getId()).toUri();
		
		return ResponseEntity.created(uri).body(saveEvent);
	}
}
