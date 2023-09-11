package se.metria.xplore.kundconfig.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import se.metria.xplore.kundconfig.entity.FastighetsokAuthEntity;
import se.metria.xplore.kundconfig.entity.MetriaMapsAuthEntity;
import se.metria.xplore.kundconfig.openapi.model.FastighetsokAuthDto;
import se.metria.xplore.kundconfig.openapi.model.MetriaMapsAuthDto;
import se.metria.xplore.kundconfig.repository.*;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    @NonNull
    private final MetriaMapsAuthRepository metriaMapsAuthRepository;

    @NonNull
    private final FastighetsokAuthRepository fastighetsokAuthRepository;

    @NonNull
    private final KundConfigRepository kundConfigRepository;

    @NonNull
    private final ModelMapper modelMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    public MetriaMapsAuthDto createMetriaMapsAuth(String kundId) {
        var kund = kundConfigRepository.findById(kundId)
            .orElseThrow(EntityNotFoundException::new);

        var entity = metriaMapsAuthRepository.saveAndFlush(new MetriaMapsAuthEntity(kund));

        return modelMapper.map(entity, MetriaMapsAuthDto.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public FastighetsokAuthDto createFastighetsokAuth(String kundId) {
        var kund = kundConfigRepository.findById(kundId)
            .orElseThrow(EntityNotFoundException::new);

        var entity = fastighetsokAuthRepository.saveAndFlush(new FastighetsokAuthEntity(kund));

        return modelMapper.map(entity, FastighetsokAuthDto.class);
    }

    public MetriaMapsAuthDto getMetriaMapsAuth(String kundId) {
        log.info("Hämtar autentiseringsuppgifter för MetriaMaps för kund: " + kundId);
        var entity = metriaMapsAuthRepository.findByKundId(kundId)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, MetriaMapsAuthDto.class);
    }

    public FastighetsokAuthDto getFastighetsokAuth(String kundId) {
        log.info("Hämtar autentiseringsuppgifter för Fastighetsök för kund: " + kundId);
        var entity = fastighetsokAuthRepository.findByKundId(kundId)
                .orElseThrow(EntityNotFoundException::new);

        return modelMapper.map(entity, FastighetsokAuthDto.class);
    }

    public void editMetriaMapsAuth(MetriaMapsAuthDto metriaMapsAuthDto) {
        log.info("Redigerar autentiseringsuppgifter för MetriaMaps: " + metriaMapsAuthDto);
        var entity = modelMapper.map(metriaMapsAuthDto, MetriaMapsAuthEntity.class);

        metriaMapsAuthRepository.saveAndFlush(entity);
    }

    public void editFastighetsokAuth(FastighetsokAuthDto fastighetsokAuthDto) {
        log.info("Redigerar autentiseringsuppgifter för Fastighetsök: " + fastighetsokAuthDto);
        var entity = modelMapper.map(fastighetsokAuthDto, FastighetsokAuthEntity.class);

        fastighetsokAuthRepository.saveAndFlush(entity);
    }
}
