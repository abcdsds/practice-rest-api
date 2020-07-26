package practicerestapi.events;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.elasticsearch.client.security.user.User;
import org.hibernate.annotations.UpdateTimestamp;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.var;
import practicerestapi.account.Account;
import practicerestapi.account.AccountAdapter;
import practicerestapi.account.CurrentUser;
import practicerestapi.index.ErrorsResource;

@Controller
@RequestMapping(value = "/api/events" , produces = MediaTypes.HAL_JSON_VALUE)
@RequiredArgsConstructor
public class EventController {

	private final EventRepository eventRepository;
	private final ModelMapper modelMapper;
	private final EventValidator eventValidator;
	
	@PostMapping
	public ResponseEntity createEvent(@RequestBody @Valid EventDto eventDto, Errors errors,
											@CurrentUser Account account) {
		
		
		//URI uri = linkTo(methodOn(EventController.class).createEvent(null)).slash("{id}").toUri();
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		eventValidator.validate(eventDto, errors);
		
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		
		Event saveEvent = eventRepository.save(modelMapper.map(eventDto, Event.class));
		saveEvent.setManager(account);
		saveEvent.freeCheck();
		saveEvent.onlineCheck();
		WebMvcLinkBuilder slash = linkTo(EventController.class).slash(saveEvent.getId());
		
		EventResource eventResource = new EventResource(saveEvent);
		eventResource.add(linkTo(EventController.class).withRel("query-events"));
		eventResource.add(slash.withRel("update-event"));
		eventResource.add(Link.of("/docs/index.html#resources-events-create").withRel("profile"));
		
		return ResponseEntity.created(slash.toUri()).body(eventResource);
	}
	
	@GetMapping
	public ResponseEntity queryEvents(Pageable pageable, PagedResourcesAssembler<Event> assemble,
			@CurrentUser Account account) {
		
		
		Page<Event> findAll = eventRepository.findAll(pageable);
		var model = assemble.toModel(findAll , e -> new EventResource(e));
		model.add(Link.of("/docs/index.html#resources-events-list").withRel("profile"));
		
		if (account != null) {
			model.add(linkTo(EventController.class).withRel("create-event"));
		}
		
		return ResponseEntity.ok(model);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity getEvent(@PathVariable Integer id, @CurrentUser Account currentUser) {
		
		
		Optional<Event> optionalEvent = eventRepository.findById(id);
		
		if (!optionalEvent.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Event event = optionalEvent.get();
		
		EventResource eventResource = new EventResource(event);
		eventResource.add(Link.of("/docs/index.html#resources-events-get").withRel("profile"));

		if (event.getOwner().equals(currentUser)) {
			eventResource.add(linkTo(EventController.class).slash(event.getId()).withRel("update-event"));
		}
		return ResponseEntity.ok(eventResource);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDto eventDto , Errors errors,
										@CurrentUser Account currentUser) {
		
		
		Optional<Event> optionalEvent = eventRepository.findById(id);
		
		if (!optionalEvent.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		if (errors.hasErrors()) {
			return badRequest(errors);
		}
		
		Event event = optionalEvent.get();
		
		if (currentUser != null && !event.getOwner().equals(currentUser)) {
			return new ResponseEntity(HttpStatus.UNAUTHORIZED);
		}
		
		eventValidator.validate(eventDto, errors);
		
		if(errors.hasErrors()) {
			return badRequest(errors);
		}
		
		
		modelMapper.map(eventDto, event);
		eventRepository.save(event);
		
		EventResource eventResource = new EventResource(event);
		eventResource.add(Link.of("/docs/index.html#resources-events-update").withRel("profile"));
		
		
		return ResponseEntity.ok(eventResource);
	}
	
	private ResponseEntity badRequest(Errors errors) {
		return ResponseEntity.badRequest().body(new ErrorsResource(errors));
	}
}
