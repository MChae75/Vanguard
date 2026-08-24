package com.vanguard.backend.retirement;

import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class RetirementResponse {
    private double totalProjectedSavings;
    private List<YearlyProjection> projections;

    @Data
    @AllArgsConstructor
    public static class YearlyProjection {
        private int age;
        private double balance;
    }
}
