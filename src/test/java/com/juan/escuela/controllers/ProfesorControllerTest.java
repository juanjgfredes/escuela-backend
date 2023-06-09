package com.juan.escuela.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.juan.escuela.dto.ProfesorDto;
import com.juan.escuela.security.TokenUtils;
import com.juan.escuela.services.ProfesorService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.co.jemos.podam.api.ClassInfoStrategy;
import uk.co.jemos.podam.api.DefaultClassInfoStrategy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfesorController.class)
class ProfesorControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ProfesorController profesorController;

    @MockBean
    private ProfesorService profesorService;

    @MockBean
    private TokenUtils tokenUtils;

    private ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private static PodamFactory podamFactory;
    private static ClassInfoStrategy classInfoStrategy;

    @BeforeAll
    static void setUpAll() {
        podamFactory = new PodamFactoryImpl();
        classInfoStrategy = DefaultClassInfoStrategy.getInstance();
        podamFactory.setClassStrategy(classInfoStrategy);
    }

    @BeforeEach
    void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup(profesorController).build();
    }

    @Test
    void getAllProfesorTest() throws Exception {

        List<ProfesorDto> profesorDtos = podamFactory.manufacturePojo(ArrayList.class, ProfesorDto.class);

        when(profesorService.getAllProfesor()).thenReturn(profesorDtos);

        mockMvc.perform(get("/v2/profesores"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id",is(profesorDtos.get(0).getId())))
                .andExpect(jsonPath("$[0].nombre",is(profesorDtos.get(0).getNombre())))
                .andExpect(jsonPath("$[1].id",is(profesorDtos.get(1).getId())))
                .andExpect(jsonPath("$[1].edad",is(profesorDtos.get(1).getEdad())));
        verify(profesorService).getAllProfesor();

    }

    @Test
    void getProfesor() throws Exception {

        ProfesorDto profesorDto = podamFactory.manufacturePojo(ProfesorDto.class);

        when(profesorService.getProfesor(anyInt())).thenReturn(profesorDto);

        mockMvc.perform(get("/v2/profesores/{id}", anyInt()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(profesorDto.getId())))
                .andExpect(jsonPath("$.apellido", is(profesorDto.getApellido())));
        verify(profesorService).getProfesor(anyInt());

    }

    @Test
    void updateProfesorTest() throws Exception {

        ProfesorDto profesorDto = podamFactory.manufacturePojo(ProfesorDto.class);

        when(profesorService.updateProfesor(profesorDto.getId(), profesorDto)).thenReturn(profesorDto);

        mockMvc.perform(put("/v2/profesores/{id}", profesorDto.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(profesorDto)))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(profesorDto.getId())))
                .andExpect(jsonPath("$.email", is(profesorDto.getEmail())));
        verify(profesorService).updateProfesor(profesorDto.getId(), profesorDto);

    }

    @Test
    void saveProfesorTest() throws Exception {

        ProfesorDto profesorDto = podamFactory.manufacturePojo(ProfesorDto.class);

        when(profesorService.saveProfesor(profesorDto)).thenReturn(profesorDto);

        mockMvc.perform(post("/v2/profesores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(profesorDto)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(profesorDto.getId())))
                .andExpect(jsonPath("$.telefono", is(profesorDto.getTelefono())));
        verify(profesorService).saveProfesor(profesorDto);

    }

    @Test
    void deleteProfesor() throws Exception {

        mockMvc.perform(delete("/v2/profesores/{id}", anyInt()))
                .andExpect(status().is(204));
        verify(profesorService).deleteProfesor(anyInt());

    }
}