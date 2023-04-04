package ru.practicum.explorewithme.compilations.repository;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.compilations.model.Compilation;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {
  List<Compilation> findAllByPinned(Boolean pinned, Pageable pageable);
}
