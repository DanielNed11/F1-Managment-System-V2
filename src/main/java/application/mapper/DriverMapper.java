package application.mapper;

import application.domain.Driver;
import application.viewmodel.DriverDTO;
import application.viewmodel.PatchDriverDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = TeamMapper.class)
public interface DriverMapper {

    @Mapping(source = "teamDTO", target = "team")
    Driver toDriver(DriverDTO driverDto);

    @Mapping(source = "team", target = "teamDTO")
    DriverDTO toDriverDTO(Driver driver);

    List<DriverDTO> toDriverDTOList(List<Driver> drivers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchDTOToDriver(PatchDriverDTO dto, @MappingTarget Driver driver);
}
