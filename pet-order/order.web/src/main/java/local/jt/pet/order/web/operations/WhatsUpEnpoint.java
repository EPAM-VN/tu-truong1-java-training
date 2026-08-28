package local.jt.pet.order.web.operations;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

@Component
@Endpoint(id = "whatsup")
public class WhatsUpEnpoint {
    @ReadOperation
    public String whatsUp() {
        return "What's Up?";
    }
}
