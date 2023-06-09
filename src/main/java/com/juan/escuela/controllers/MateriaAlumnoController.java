package com.juan.escuela.controllers;

import com.juan.escuela.dto.MateriaNotaDto;
import com.juan.escuela.services.MateriaAlumnoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v2")
@Tag(name = "Materia-Alumno")
public class MateriaAlumnoController {

    private final MateriaAlumnoService materiaAlumnoService;

    @PostMapping("/alumnos/{idAlumno}/materias/{idMateria}")
    public ResponseEntity<MateriaNotaDto> updateMateriaAlumno(@PathVariable("idAlumno") int idAlumno, @PathVariable("idMateria") int idMateria, @Valid @RequestBody MateriaNotaDto materiaNotaDto){
        return ResponseEntity.ok(materiaAlumnoService.updateMateriaNota(idAlumno, idMateria, materiaNotaDto));
    }
}
