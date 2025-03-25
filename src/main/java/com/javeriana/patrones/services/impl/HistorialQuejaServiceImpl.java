package com.javeriana.patrones.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.javeriana.patrones.Dtos.HistorialQuejaDTO;
import com.javeriana.patrones.model.HistorialQueja;
import com.javeriana.patrones.repository.HistorialQuejaRepository;
import com.javeriana.patrones.services.HistorialQuejaService;
@Service
public class HistorialQuejaServiceImpl implements HistorialQuejaService {
    private final HistorialQuejaRepository repository;
    private final ModelMapper modelMapper;

    public HistorialQuejaServiceImpl(HistorialQuejaRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public HistorialQuejaDTO registrarHistorial(HistorialQuejaDTO dto) {
        HistorialQueja historial = modelMapper.map(dto, HistorialQueja.class);
        historial.setFechaCambio(LocalDateTime.now());
        return modelMapper.map(repository.save(historial), HistorialQuejaDTO.class);
    }

    @Override
    public List<HistorialQuejaDTO> listarPorQueja(Long quejaId) {
        return repository.findByQuejaId(quejaId).stream()
                .map(historial -> modelMapper.map(historial, HistorialQuejaDTO.class))
                .collect(Collectors.toList());
    }
}
