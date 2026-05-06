package application.mapper;

import application.api.dto.AddDriverDTO;
import application.api.dto.DriverDTO;
import application.api.dto.PatchDriverDTO;
import application.controller.viewmodel.AddDriverViewModel;
import application.controller.viewmodel.DriverViewModel;
import application.controller.viewmodel.PatchDriverViewModel;
import application.domain.Driver;
import application.service.command.UpdateDriverCommand;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", uses = TeamMapper.class)
public interface DriverMapper {

    @Mapping(source = "team", target = "simpleTeamDTO")
    DriverDTO toDriverDTO(Driver driver);

    List<DriverDTO> toDriverDTOList(List<Driver> drivers);

    @Mapping(source = "teamViewModel", target = "team")
    Driver toDriver(DriverViewModel driverDto);

    @Mapping(source = "team", target = "teamViewModel")
    DriverViewModel toDriverViewModel(Driver driver);

    @Mapping(target = "team", ignore = true)
    Driver toDriver(AddDriverViewModel addDriverViewModel);

    @Mapping(target = "team", ignore = true)
    Driver toDriver(AddDriverDTO addDriverDTO);

    @Mapping(source = "team.id", target = "teamId")
    PatchDriverViewModel toPatchDriverViewModel(Driver driver);

    List<DriverViewModel> toDriverViewModelList(List<Driver> drivers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UpdateDriverCommand toUpdateDriverCommand(PatchDriverViewModel viewModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UpdateDriverCommand toUpdateDriverCommand(PatchDriverDTO dto);

}
