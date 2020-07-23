package practicerestapi.events;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;


class EventTest {

	private static Stream<Arguments> domainFreeTest() {
		return Stream.of(
					Arguments.of(0 , 0 , true),
					Arguments.of(100 , 0 , false),
					Arguments.of(0 , 100 , false)
				);
	}
	
	private static Stream<Arguments> domainOfflineTest() {
		return Stream.of(
					Arguments.of("rrrr" , true),
					Arguments.of(null ,  false)
				);
	}
	
	@ParameterizedTest
	@MethodSource
	public void domainFreeTest(int basePrice, int maxPrice, boolean isFree) {
		// given
		Event event = Event.builder()
				.basePrice(basePrice)
				.maxPrice(maxPrice)
				.build();
		
		// when
		event.freeCheck();
		
		// then
		assertThat(event.isFree()).isEqualTo(isFree);

	}
	
	@ParameterizedTest
	@MethodSource
	public void domainOfflineTest(String location, boolean isOffline) {	
		
		// given
		Event event = Event.builder()
				.location(location)
				.build();
		
		// when
		event.onlineCheck();
		
		// then
		assertThat(event.isOffline()).isEqualTo(isOffline);
	}
	

}
