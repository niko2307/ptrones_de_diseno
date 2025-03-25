package com.javeriana.patrones.services.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.javeriana.patrones.Dtos.EmpresaDTO;
import com.javeriana.patrones.model.EmpresaVigilada;
import com.javeriana.patrones.repository.EmpresaVigiladaRepository;
import com.javeriana.patrones.services.EmpresaVigiladaService;

import jakarta.persistence.EntityNotFoundException;
@Service
public class EmpresaVigiladaServiceImpl implements EmpresaVigiladaService {
    private final EmpresaVigiladaRepository repository;
    private final ModelMapper modelMapper;

    public EmpresaVigiladaServiceImpl(EmpresaVigiladaRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EmpresaDTO registrarEmpresa(EmpresaDTO dto) {
        EmpresaVigilada empresa = modelMapper.map(dto, EmpresaVigilada.class);
        return modelMapper.map(repository.save(empresa), EmpresaDTO.class);
    }

    @Override
    public EmpresaDTO obtenerPorId(Long id) {
        return repository.findById(id)
                .map(empresa -> modelMapper.map(empresa, EmpresaDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Empresa no encontrada"));
    }

    @Override
    public List<EmpresaDTO> listarEmpresas() {
        return repository.findAll().stream()
                .map(empresa -> modelMapper.map(empresa, EmpresaDTO.class))
                .collect(Collectors.toList());
    }
}
