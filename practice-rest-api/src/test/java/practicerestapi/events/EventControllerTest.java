package practicerestapi.events;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.stream.IntStream;

import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import practicerestapi.common.RestDocsConfiguration;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;


@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
class EventControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ModelMapper modelMapper;
	
	@Autowired
	EventRepository eventRepository;

	@Test
	public void createEvent() throws Exception {

		EventDto event = EventDto.builder().name("spring").description("REST API")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 20, 1, 1, 1))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 21, 1, 1, 1))
				.beginEventDateTime(LocalDateTime.of(2020, 7, 22, 1, 1, 1))
				.endEventDateTime(LocalDateTime.of(2020, 7, 23, 1, 1, 1)).basePrice(100).maxPrice(200)
				.limitOfEnrollment(100).location("강남").build();

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))).andDo(print()).andExpect(status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("id").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("free").value(false))
				.andExpect(MockMvcResultMatchers.jsonPath("offline").value(true))
				.andExpect(MockMvcResultMatchers.jsonPath("eventStatus").value("DRAFT"))
				.andExpect(MockMvcResultMatchers.jsonPath("_links.self").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("_links.query-events").exists())
				.andExpect(MockMvcResultMatchers.jsonPath("_links.update-event").exists())
				.andDo(document("create-event" , 
							links(
									linkWithRel("self").description("link to self"),
									linkWithRel("query-events").description("link to query events"),
									linkWithRel("update-event").description("link to update an existing event"),
									linkWithRel("profile").description("profile")
									
							),
							requestHeaders(
									headerWithName(HttpHeaders.ACCEPT).description("accept header"),
									headerWithName(HttpHeaders.CONTENT_TYPE).description("content type header")
							),
							requestFields(
									fieldWithPath("name").description("Name of new event"),
	                                fieldWithPath("description").description("description of new event"),
	                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
	                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
	                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
	                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
	                                fieldWithPath("location").description("location of new event"),
	                                fieldWithPath("basePrice").description("base price of new event"),
	                                fieldWithPath("maxPrice").description("max price of new event"),
	                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment")
							),
							responseHeaders(
									headerWithName(HttpHeaders.LOCATION).description("Location header"),
	                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content type")
							),
							//relaxed 사용시 일부만 문서화 가능
							responseFields(
									fieldWithPath("id").description("identifier of new event"),
	                                fieldWithPath("name").description("Name of new event"),
	                                fieldWithPath("description").description("description of new event"),
	                                fieldWithPath("beginEnrollmentDateTime").description("date time of begin of new event"),
	                                fieldWithPath("closeEnrollmentDateTime").description("date time of close of new event"),
	                                fieldWithPath("beginEventDateTime").description("date time of begin of new event"),
	                                fieldWithPath("endEventDateTime").description("date time of end of new event"),
	                                fieldWithPath("location").description("location of new event"),
	                                fieldWithPath("basePrice").description("base price of new event"),
	                                fieldWithPath("maxPrice").description("max price of new event"),
	                                fieldWithPath("limitOfEnrollment").description("limit of enrolmment"),
	                                fieldWithPath("free").description("it tells if this event is free or not"),
	                                fieldWithPath("offline").description("it tells if this event is offline event or not"),
	                                fieldWithPath("eventStatus").description("event status"),
	                                fieldWithPath("_links.self.href").description("link to self"),
	                                fieldWithPath("_links.query-events.href").description("link to query event list"),
	                                fieldWithPath("_links.update-event.href").description("link to update existing event"),
	                                fieldWithPath("_links.profile.href").description("link to profile")
							)
							
				));
		
	}

	@Test
	public void test_badRequest() throws Exception {

		Event event = Event.builder().name("spring").id(100).description("REST API")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 20, 1, 1, 1))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 21, 1, 1, 1))
				.beginEventDateTime(LocalDateTime.of(2020, 7, 22, 1, 1, 1))
				.endEventDateTime(LocalDateTime.of(2020, 7, 23, 1, 1, 1)).basePrice(100).maxPrice(200)
				.limitOfEnrollment(100).location("강남").free(true).eventStatus(EventStatus.PUBLISHED).build();

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(event))).andDo(print()).andExpect(status().isBadRequest());

	}

	@Test
	public void test_badRequest_Empty_input() throws Exception {

		EventDto eventDto = EventDto.builder().build();

		mockMvc.perform(post("/api/events").contentType(MediaType.APPLICATION_JSON).accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))).andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void test_badRequest_Wrong_input() throws Exception {
		
		EventDto eventDto = EventDto.builder().name("spring")
				.description("REST API")
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 7 , 20 , 1 ,1 , 1))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 21 , 1, 1, 1))
				.beginEventDateTime(LocalDateTime.of(2020, 7 , 22 , 1 ,1 ,1))
				.endEventDateTime(LocalDateTime.of(2020, 7 , 21, 1, 1, 1))
				.basePrice(10000)
				.maxPrice(200)
				.limitOfEnrollment(100)
				.location("강남")
				.build();
		
		mockMvc.perform(
				post("/api/events")						
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaTypes.HAL_JSON)
				.content(objectMapper.writeValueAsString(eventDto))
				)
			.andDo(print())
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("content[0].objectName").exists())
			.andExpect(jsonPath("content[0].defaultMessage").exists())
			.andExpect(jsonPath("content[0].code").exists())
			.andExpect(jsonPath("_links.index").exists());
		
	}
	
	@Test
	public void queryEvents() throws Exception {
		
		IntStream.range(0, 30).forEach(this::generateEvent);
		
		mockMvc.perform(get("/api/events")
					.param("page", "1")
					.param("size", "10")
					.param("sort", "name,DESC")
				)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("page").exists()) 
				.andExpect(jsonPath("_embedded.eventResourceList[0]._links.self").exists())
				.andExpect(jsonPath("_links.self").exists())
				.andExpect(jsonPath("_links.profile").exists())
				.andDo(document("events-list"))
				;
	}
	
	@Test
	public void getEvent404() throws Exception {
		
		Event generateEvent = generateEvent(100);
		
		mockMvc.perform(get("/api/events/111112" , generateEvent.getId()))
					.andExpect(status().isNotFound());
				
	}
	
	@Test
	public void getEvent() throws Exception {
		
		Event generateEvent = generateEvent(100);
		
		mockMvc.perform(get("/api/events/{id}" , generateEvent.getId()))
					.andExpect(status().isOk())
					.andExpect(jsonPath("name").exists())
					.andExpect(jsonPath("id").exists())
					.andDo(document("get-an-event"));
				
	}
	
	@Test
	public void updateEvent() throws Exception {
		
		Event generateEvent = generateEvent(100);
		EventDto eventDto = modelMapper.map(generateEvent, EventDto.class);
		eventDto.setName("change name");
		
		mockMvc.perform(put("/api/events/{id}" , generateEvent.getId())
							.contentType(MediaType.APPLICATION_JSON)
							.content(objectMapper.writeValueAsString(eventDto))
					)
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("name").exists())
					.andExpect(jsonPath("id").exists())
					.andDo(document("get-an-event"));
				
	}
	
	@Test
	public void updateEvent404empty() throws Exception {
		
		Event generateEvent = generateEvent(100);
		EventDto eventDto = new EventDto();
		
		mockMvc.perform(put("/api/events/{id}" , generateEvent.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(eventDto))
					)
					.andExpect(status().isBadRequest());
				
	}
	
	
	@Test
	public void updateEvent404wrong() throws Exception {
		
		Event generateEvent = generateEvent(100);
		EventDto eventDto = modelMapper.map(generateEvent, EventDto.class);
		eventDto.setBasePrice(20000);
		eventDto.setMaxPrice(1000);
		
		mockMvc.perform(put("/api/events/{id}" , generateEvent.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(eventDto))
					)
					.andExpect(status().isBadRequest());
	}
	
	@Test
	public void updateEvent404() throws Exception {
		
		Event generateEvent = generateEvent(100);
		EventDto eventDto = modelMapper.map(generateEvent, EventDto.class);
		
		mockMvc.perform(put("/api/events/1123123123")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(eventDto))
					)
					.andExpect(status().isNotFound());
	}

	private Event generateEvent(int i) {
		// TODO Auto-generated method stub
		
		Event event = Event.builder()
				.name("spring" + i)
				.description("REST API" + i)
				.beginEnrollmentDateTime(LocalDateTime.of(2020, 7, 20, 1, 1, 1))
				.closeEnrollmentDateTime(LocalDateTime.of(2020, 7, 21, 1, 1, 1))
				.beginEventDateTime(LocalDateTime.of(2020, 7, 22, 1, 1, 1))
				.endEventDateTime(LocalDateTime.of(2020, 7, 23, 1, 1, 1))
				.basePrice(100)
				.maxPrice(200)
				.free(false)
				.offline(true)
				.eventStatus(EventStatus.DRAFT)
				.limitOfEnrollment(100)
				.location("강남")
				.build();
		
		return eventRepository.save(event);
	}
	
	

}

