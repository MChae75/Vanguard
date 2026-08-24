package com.vanguard.backend.portfolio;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String symbol;
    
    private String name;
    private double quantity;
    private String category;
    
    @Transient
    private double currentPrice;
    
    @Transient
    private double value;
}
