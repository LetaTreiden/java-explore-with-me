package ru.practicum.explorewithme.compilations.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilations.model.CompilationEntity;

public interface CompilationRepository extends JpaRepository<CompilationEntity, Long> {

  List<CompilationEntity> findAllByPinned(Boolean pinned, Pageable pageable);
}
