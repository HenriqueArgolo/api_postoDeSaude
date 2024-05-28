package argolo.tech.springsecurity6.controller;

import argolo.tech.springsecurity6.controller.dto.ProceduresDto;
import argolo.tech.springsecurity6.entities.Procedures;
import argolo.tech.springsecurity6.repository.ProceduresRepositiry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ProceduresController {


    private final ProceduresRepositiry proceduresRepositiry;

    public ProceduresController(ProceduresRepositiry proceduresRepositiry) {
        this.proceduresRepositiry = proceduresRepositiry;
    }

    @PostMapping("/createProcedures")
    private ResponseEntity<Void> createProcedures(@RequestBody ProceduresDto proceduresDto) {
        var procedure = new Procedures();
        procedure.setName(proceduresDto.name());

        proceduresRepositiry.save(procedure);
        procedure.setStatus("Disponível".toLowerCase());

        return ResponseEntity.ok().build();
    }
}
