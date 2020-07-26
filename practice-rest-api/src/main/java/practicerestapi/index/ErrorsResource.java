package practicerestapi.index;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.validation.Errors;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


public class ErrorsResource extends RepresentationModel<ErrorsResource>{

	@JsonUnwrapped
	private Errors errors;
	
	public ErrorsResource(Errors errors) {
		this.errors = errors;
		add(linkTo(IndexController.class).withRel("index"));
	}

}
