package com.juan.escuela.services;

import com.juan.escuela.dto.MateriaDetailsDto;
import com.juan.escuela.dto.MateriaDto;
import com.juan.escuela.exceptions.AppException;
import com.juan.escuela.mappers.MateriaMapper;
import com.juan.escuela.models.Materia;
import com.juan.escuela.repositories.MateriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MateriaService {

    private final MateriaRepository materiaRepository;

    private final MateriaMapper materiaMapper;

    public List<MateriaDto> getAllMateria() {
        List<Materia> materias = (List<Materia>) materiaRepository.findAll();
        return materiaMapper.toListMateriaDto(materias);
    }

    public MateriaDetailsDto getMateria(int id) {
        Materia materia = materiaRepository.findById(id)
                .orElseThrow(() -> new AppException("no se encontro el alumno con el id: " + id, HttpStatus.NOT_FOUND));
        return materiaMapper.toMateriaDetailsDto(materia);
    }

    public MateriaDto saveMateria(MateriaDto materia) {
        Materia materiaSave = materiaRepository.save(materiaMapper.toMateria(materia));
        return materiaMapper.toMateriaDto(materiaSave);
    }

    public void deleteMateria(int id){
        if(!materiaRepository.existsById(id)) {
            throw new AppException("no se pudo eliminar porque no se encontro el alumno con el id: " + id, HttpStatus.NOT_FOUND);
        }

        materiaRepository.deleteById(id);
    }

    public MateriaDto updateMateria(int id, MateriaDto materiaDto){
        Materia newMateria = materiaMapper.toMateria(materiaDto);
        Materia materiaPut = materiaRepository.findById(id)
                .map(materia -> {
                    materia.setId(id);
                    materia.setNombre(newMateria.getNombre());
                    return materiaRepository.save(materia);})
                .orElseThrow(() -> new AppException("no se encontro la materia con el id: " + id, HttpStatus.NOT_FOUND));

        return materiaMapper.toMateriaDto(materiaPut);
    }

}
