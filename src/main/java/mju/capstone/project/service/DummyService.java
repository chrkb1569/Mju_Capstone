package mju.capstone.project.service;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.domain.Dummy;
import mju.capstone.project.dto.DummyDto;
import mju.capstone.project.exception.DummyNotFoundException;
import mju.capstone.project.repository.DummyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DummyService {

    private final DummyRepository dummyRepository;

    @Transactional(readOnly = true)
    public List<DummyDto> getList() {
        return dummyRepository.findAll()
                .stream().map(Dummy::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DummyDto getDummy(Long id) {
        Dummy dummy = dummyRepository.findById(id).orElseThrow(DummyNotFoundException::new);

        return dummy.toDto();
    }
}
