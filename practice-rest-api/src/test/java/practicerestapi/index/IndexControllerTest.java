package practicerestapi.index;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import practicerestapi.common.BaseTest;
import practicerestapi.common.RestDocsConfiguration;

public class IndexControllerTest extends BaseTest {
	
	@Test
	public void index() throws Exception {
		mockMvc.perform(get("/api/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_links.events").exists());
			
	}
}
