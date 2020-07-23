package practicerestapi.events;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @EqualsAndHashCode(of = "id")
@Builder @NoArgsConstructor @AllArgsConstructor
public class Event {

	@Id @GeneratedValue
	private Integer id;
	
	private String name;
    private String description;
    private LocalDateTime beginEnrollmentDateTime;
    private LocalDateTime closeEnrollmentDateTime;
    private LocalDateTime beginEventDateTime;
    private LocalDateTime endEventDateTime;
    private String location; // (optional) 이게 없으면 온라인 모임
    private int basePrice; // (optional)
    private int maxPrice; // (optional)
    private int limitOfEnrollment;
    private boolean offline;
    private boolean free;
    
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus = EventStatus.DRAFT;


    
    
    public void onlineCheck() {
    	if (this.location == null) {
    		this.offline = false; 
    	} else {
    		this.offline = true;
    	}
    }
    
    public void freeCheck() {
    	if (this.basePrice == 0 && this.maxPrice == 0) {
    		this.free = true;
    	} else {
    		this.free = false;
    	}
    }

}
