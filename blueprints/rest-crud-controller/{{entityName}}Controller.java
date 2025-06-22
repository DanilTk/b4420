package {{package}}.api.web.{{entityNameLower}};

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/{{entityNameLower}}s")
@RequiredArgsConstructor
@Tag(name = "{{entityName}} API", description = "CRUD API for {{entityName}} entity.")
public class {{entityName}}Controller {

    private final {{entityName}}Handler handler;

    @GetMapping
    @Operation(summary = "List all {{entityNameLower}}s")
    public List<{{entityName}}ResponseDto> listAll(@Valid @ParameterObject {{entityName}}FilterDto filter) {
        return handler.listAll(filter);
    }

    @GetMapping("{uuid}")
    @Operation(summary = "Get one {{entityNameLower}} by UUID")
    public {{entityName}}ResponseDto get(@PathVariable UUID uuid) {
        return handler.get(uuid);
    }

    @PostMapping
    @Operation(summary = "Create new {{entityNameLower}}")
    public {{entityName}}ResponseDto create(@RequestBody @Valid {{entityName}}CreateDto dto) {
        return handler.create(dto);
    }

    @PutMapping("{uuid}")
    @Operation(summary = "Update existing {{entityNameLower}}")
    public {{entityName}}ResponseDto update(@PathVariable UUID uuid,
                                            @RequestBody @Valid {{entityName}}UpdateDto dto) {
        return handler.update(uuid, dto);
    }

    @DeleteMapping("{uuid}")
    @Operation(summary = "Delete {{entityNameLower}} by UUID")
    public void delete(@PathVariable UUID uuid) {
        handler.delete(uuid);
    }
}
