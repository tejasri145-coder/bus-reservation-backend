package com.busreservation.backend.service.impl;

import com.busreservation.backend.dto.BusDTO;
import com.busreservation.backend.entity.Bus;
import com.busreservation.backend.exception.BadRequestException;
import com.busreservation.backend.exception.ResourceNotFoundException;
import com.busreservation.backend.repository.BusRepository;
import com.busreservation.backend.service.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BusServiceImpl implements BusService {

    private final BusRepository busRepository;

    @Override
    @Transactional
    public BusDTO createBus(BusDTO busDTO) {
        if (busRepository.existsByBusNumber(busDTO.getBusNumber())) {
            throw new BadRequestException("Bus with number " + busDTO.getBusNumber() + " already exists");
        }

        Bus bus = Bus.builder()
                .busNumber(busDTO.getBusNumber().trim().toUpperCase())
                .operatorName(busDTO.getOperatorName())
                .busType(busDTO.getBusType())
                .totalSeats(busDTO.getTotalSeats())
                .active(busDTO.getActive() != null ? busDTO.getActive() : true)
                .build();

        Bus saved = busRepository.save(bus);
        return mapToDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public BusDTO getBusById(Long id) {
        Bus bus = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));
        return mapToDTO(bus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BusDTO> getAllBuses() {
        return busRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public BusDTO updateBus(Long id, BusDTO busDTO) {
        Bus existing = busRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found with id: " + id));

        if (!existing.getBusNumber().equalsIgnoreCase(busDTO.getBusNumber()) &&
                busRepository.existsByBusNumber(busDTO.getBusNumber())) {
            throw new BadRequestException("Bus with number " + busDTO.getBusNumber() + " already exists");
        }

        existing.setBusNumber(busDTO.getBusNumber().trim().toUpperCase());
        existing.setOperatorName(busDTO.getOperatorName());
        existing.setBusType(busDTO.getBusType());
        existing.setTotalSeats(busDTO.getTotalSeats());
        if (busDTO.getActive() != null) {
            existing.setActive(busDTO.getActive());
        }

        Bus updated = busRepository.save(existing);
        return mapToDTO(updated);
    }

    @Override
    @Transactional
    public void deleteBus(Long id) {
        if (!busRepository.existsById(id)) {
            throw new ResourceNotFoundException("Bus not found with id: " + id);
        }
        busRepository.deleteById(id);
    }

    private BusDTO mapToDTO(Bus bus) {
        return BusDTO.builder()
                .id(bus.getId())
                .busNumber(bus.getBusNumber())
                .operatorName(bus.getOperatorName())
                .busType(bus.getBusType())
                .totalSeats(bus.getTotalSeats())
                .active(bus.getActive())
                .build();
    }
}
