package practicerestapi.common;

import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import practicerestapi.account.AccountRepository;
import practicerestapi.account.AccountService;
import practicerestapi.commons.AppProperties;
import practicerestapi.events.EventRepository;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@SpringBootTest
@Import(RestDocsConfiguration.class)
@ActiveProfiles("test")
@Disabled
public class BaseTest {

	@Autowired
	protected MockMvc mockMvc;

	@Autowired
	protected ObjectMapper objectMapper;
	
	@Autowired
	protected ModelMapper modelMapper;
	
	@Autowired
	protected EventRepository eventRepository;
	
	@Autowired
	protected AccountService accountService;
	
	@Autowired
	protected AccountRepository accountRepository;
	
	@Autowired
	protected AppProperties appProperties;
}
