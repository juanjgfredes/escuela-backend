package com.juan.escuela.services;

import com.juan.escuela.dto.ProfesorDto;
import com.juan.escuela.exceptions.AppException;
import com.juan.escuela.mappers.ProfesorMapper;
import com.juan.escuela.models.Profesor;
import com.juan.escuela.repositories.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesorService {

    @Autowired
    private ProfesorRepository profesorRepository;

    @Autowired
    private ProfesorMapper profesorMapper;

    public List<ProfesorDto> getAllProfesores(){
        List<Profesor> profesorDtos = (List<Profesor>) profesorRepository.findAll();
        return profesorMapper.toListProfesorDto(profesorDtos);
    }

    public ProfesorDto getProfesorById(int id) {
        Profesor profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new AppException("el profesor con el id: " + id + " no existe", HttpStatus.BAD_REQUEST));
        return profesorMapper.toProfesorDto(profesor);
    }

    public ProfesorDto saveProfesor(Profesor profesor) {
        System.out.println(profesor);
        Profesor profesorSave = profesorRepository.save(profesor);
        return profesorMapper.toProfesorDto(profesorSave);
    }

    public void deleteProfesorByid(int id){
        if (!profesorRepository.existsById(id)) {
            throw new AppException("el profesor con el id: " + id + " no existe", HttpStatus.BAD_REQUEST);
        }

        profesorRepository.deleteById(id);
    }

}
