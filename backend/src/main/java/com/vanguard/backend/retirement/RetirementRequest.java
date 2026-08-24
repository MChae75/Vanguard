package com.vanguard.backend.retirement;

import lombok.Data;

@Data
public class RetirementRequest {
    private int currentAge;
    private int retirementAge;
    private double currentSavings;
    private double monthlyContribution;
    private double expectedAnnualReturn; // e.g. 0.07 for 7%
}
