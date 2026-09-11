package local.jt.pet.order.web.mappers;

import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.ERROR,
        builder = @org.mapstruct.Builder(disableBuilder = true)
)
public interface AvroMapperConfig {

}
