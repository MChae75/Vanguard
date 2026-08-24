package com.vanguard.backend.retirement;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/retirement")
@CrossOrigin(origins = "*")
public class RetirementController {

    private final RetirementService retirementService;

    public RetirementController(RetirementService retirementService) {
        this.retirementService = retirementService;
    }

    @PostMapping("/calculate")
    public RetirementResponse calculate(@RequestBody RetirementRequest request) {
        return retirementService.calculateRetirement(request);
    }
}
