package practicerestapi.events;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
class EventControllerTest {

	@Autowired
	MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;

	
	@Test
	public void test() throws Exception {
		
		EventDto event = EventDto.builder().name("spring")
						.description("REST API")
						.beginEnrollmentDateTime(LocalDateTime.of(2020, 7 , 20 , 1 ,1 , 1))
						.closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 21 , 1, 1, 1))
						.beginEventDateTime(LocalDateTime.of(2020, 7 , 22 , 1 ,1 ,1))
						.endEventDateTime(LocalDateTime.of(2020, 7 , 23, 1, 1, 1))
						.basePrice(100)
						.maxPrice(200)
						.limitOfEnrollment(100)
						.location("강남")
						.build();
		
		mockMvc.perform(
						post("/api/events")						
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event))
						)
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(MockMvcResultMatchers.jsonPath("id").exists())
			.andExpect(MockMvcResultMatchers.jsonPath("id").value(Matchers.not(100)))
			.andExpect(MockMvcResultMatchers.jsonPath("free").value(Matchers.not(true)));
	}
	
	@Test
	public void test_badRequest() throws Exception {
		
		Event event = Event.builder().name("spring")
						.id(100)
						.description("REST API")
						.beginEnrollmentDateTime(LocalDateTime.of(2020, 7 , 20 , 1 ,1 , 1))
						.closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 21 , 1, 1, 1))
						.beginEventDateTime(LocalDateTime.of(2020, 7 , 22 , 1 ,1 ,1))
						.endEventDateTime(LocalDateTime.of(2020, 7 , 23, 1, 1, 1))
						.basePrice(100)
						.maxPrice(200)
						.limitOfEnrollment(100)
						.location("강남")
						.free(true)
						.eventStatus(EventStatus.PUBLISHED)
						.build();
		
		mockMvc.perform(
						post("/api/events")						
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaTypes.HAL_JSON)
						.content(objectMapper.writeValueAsString(event))
						)
			.andDo(print())
			.andExpect(status().isBadRequest());
		
	}

}
