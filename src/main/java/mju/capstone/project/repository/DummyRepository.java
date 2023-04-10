package mju.capstone.project.repository;

import mju.capstone.project.domain.Dummy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DummyRepository extends JpaRepository<Dummy, Long> {
}

