package com.javeriana.patrones.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.javeriana.patrones.Dtos.QuejaDTO;
import com.javeriana.patrones.model.EstadoQueja;
import com.javeriana.patrones.model.Queja;
import com.javeriana.patrones.repository.QuejaRepository;
import com.javeriana.patrones.services.QuejaService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class QuejaServiceImpl implements QuejaService {
    private final QuejaRepository repository;
    private final ModelMapper modelMapper;

    public QuejaServiceImpl(QuejaRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public QuejaDTO registrarQueja(QuejaDTO dto) {
        Queja queja = modelMapper.map(dto, Queja.class);
        queja.setFechaRegistro(LocalDateTime.now());
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
