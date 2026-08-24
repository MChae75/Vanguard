package com.vanguard.backend.portfolio;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Asset, Long> {
    Optional<Asset> findBySymbol(String symbol);
}
