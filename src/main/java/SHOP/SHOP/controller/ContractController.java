package SHOP.SHOP.controller;

import SHOP.SHOP.dto.ContractRequest;
import SHOP.SHOP.service.SoapService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Contract")

public class ContractController {

    private final SoapService soapService;

    public ContractController(SoapService soapService) {
        this.soapService = soapService;
    }

    @PostMapping("/Contract")
    public String getContractInfo(@RequestBody ContractRequest request) {
        return soapService.callSoap(
                request.getContractRefName(),
                request.getContractRefNumber()
        );
    }
}
