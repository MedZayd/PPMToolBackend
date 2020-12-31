package io.med.ppmtool.mappers;

import io.med.ppmtool.domain.Project;
import io.med.ppmtool.dto.ProjectDto;
import org.mapstruct.*;

@Mapper
public interface ProjectMapper {
    Project toEntity(ProjectDto dto);

    @Mapping(target = "identifier", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProjectFromDto(ProjectDto dto, @MappingTarget Project project);
}
