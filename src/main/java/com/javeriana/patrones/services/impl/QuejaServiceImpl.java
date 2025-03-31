package com.javeriana.patrones.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.javeriana.patrones.Dtos.QuejaDTO;
import com.javeriana.patrones.model.EmpresaVigilada;
import com.javeriana.patrones.model.EstadoQueja;
import com.javeriana.patrones.model.Queja;
import com.javeriana.patrones.model.Usuario;
import com.javeriana.patrones.repository.EmpresaVigiladaRepository;
import com.javeriana.patrones.repository.QuejaRepository;
import com.javeriana.patrones.repository.UsuarioRepository;
import com.javeriana.patrones.services.QuejaService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class QuejaServiceImpl implements QuejaService {
    private final QuejaRepository repository;
    private final ModelMapper modelMapper;
    private final EmpresaVigiladaRepository empresaRepository;
    private final UsuarioRepository usuarioRepository;


    public QuejaServiceImpl(QuejaRepository repository, 
    EmpresaVigiladaRepository empresaRepository,
    UsuarioRepository usuarioRepository,
    ModelMapper modelMapper) {
this.repository = repository;
this.empresaRepository = empresaRepository;
this.usuarioRepository = usuarioRepository;
this.modelMapper = modelMapper;
}

   
  @Override
public QuejaDTO registrarQueja(QuejaDTO dto) {
    Queja queja = new Queja();

    // Asignar campos simples
    queja.setDescripcion(dto.getDescripcion());
    queja.setEstado(EstadoQueja.valueOf(dto.getEstado().toUpperCase()));
    queja.setFechaRegistro(LocalDateTime.now());

    // Asignar relaciones desde los IDs
    EmpresaVigilada empresa = empresaRepository.findById(dto.getEmpresaId())
            .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    queja.setEmpresaVigilada(empresa);

    Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
    queja.setUsuario(usuario);

    return modelMapper.map(repository.save(queja), QuejaDTO.class);
}


    @Override
    public List<QuejaDTO> listarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId).stream()
                .map(queja -> modelMapper.map(queja, QuejaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<QuejaDTO> listarPorEstado(String estado) {
        EstadoQueja estadoQueja = EstadoQueja.valueOf(estado.toUpperCase());
        return repository.findByEstado(estadoQueja).stream()
                .map(queja -> modelMapper.map(queja, QuejaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public QuejaDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(q -> modelMapper.map(q, QuejaDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Queja no encontrada"));
    }
}
