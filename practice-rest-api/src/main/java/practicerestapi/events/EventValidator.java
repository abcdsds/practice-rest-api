package practicerestapi.events;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

@Component
public class EventValidator{

	public void validate(EventDto eventDto, Errors errors) {
		if (eventDto.getBasePrice() > eventDto.getMaxPrice() && eventDto.getMaxPrice() != 0) {
			errors.rejectValue("basePrice", "wrongValue" , "BasePrice cannot Lager Than MaxPrice.");
			errors.rejectValue("maxPrice", "wrongValue" , "MaxPrice cannot Smaller Than BasePrice.");
		}
		
		LocalDateTime endEventDateTime = eventDto.getEndEventDateTime();
		if (endEventDateTime.isBefore(eventDto.getBeginEventDateTime()) 
				&& endEventDateTime.isBefore(eventDto.getBeginEnrollmentDateTime())
				&& endEventDateTime.isBefore(eventDto.getCloseEnrollmentDateTime())) {
			
			errors.rejectValue("endEventDateTime", "wrongValue" , "endEventDateTime is Wrong Value!");
		}
		
		
	}

}
