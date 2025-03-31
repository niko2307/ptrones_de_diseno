package com.javeriana.patrones.services.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.javeriana.patrones.Dtos.HistorialQuejaDTO;
import com.javeriana.patrones.model.EstadoQueja;
import com.javeriana.patrones.model.HistorialQueja;
import com.javeriana.patrones.model.Queja;
import com.javeriana.patrones.repository.HistorialQuejaRepository;
import com.javeriana.patrones.repository.QuejaRepository;
import com.javeriana.patrones.services.HistorialQuejaService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class HistorialQuejaServiceImpl implements HistorialQuejaService {
   private final HistorialQuejaRepository repository;
    private final QuejaRepository quejaRepository;
    private final ModelMapper modelMapper;

    public HistorialQuejaServiceImpl(HistorialQuejaRepository repository, 
                                     QuejaRepository quejaRepository, 
                                     ModelMapper modelMapper) {
        this.repository = repository;
        this.quejaRepository = quejaRepository;
        this.modelMapper = modelMapper;
    }

   

    @Override
   public HistorialQuejaDTO registrarHistorial(HistorialQuejaDTO dto) {
        Queja queja = quejaRepository.findById(dto.getQuejaId())
            .orElseThrow(() -> new EntityNotFoundException("Queja no encontrada"));

        HistorialQueja historial = new HistorialQueja();
        historial.setQueja(queja);
        historial.setEstadoAnterior(EstadoQueja.valueOf(dto.getEstadoAnterior()));
        historial.setEstadoNuevo(EstadoQueja.valueOf(dto.getEstadoNuevo()));
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
