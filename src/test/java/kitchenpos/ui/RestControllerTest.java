package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class RestControllerTest {

    public String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
