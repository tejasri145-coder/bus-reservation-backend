package com.busreservation.backend.service;

import com.busreservation.backend.dto.BusDTO;

import java.util.List;

public interface BusService {
    BusDTO createBus(BusDTO busDTO);
    BusDTO getBusById(Long id);
    List<BusDTO> getAllBuses();
    BusDTO updateBus(Long id, BusDTO busDTO);
    void deleteBus(Long id);
}
