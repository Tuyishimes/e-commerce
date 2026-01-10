package SHOP.SHOP.service;

import SHOP.SHOP.model.CConfigProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class SoapService {

    private final WebClient webClient;
    private final CConfigProperties props;

    public SoapService(CConfigProperties props) {
        this.props = props;
        this.webClient = WebClient.builder()
                .baseUrl(props.getUrl())
                .defaultHeader("Content-Type", "text/xml")
                .defaultHeader("Accept-Charset", "UTF-8")
                .defaultHeader("SOAPAction", "urn:getContractInformation")
                .build();
    }

    public String callSoap(String contractRefName, String contractRefNumber) {

        String xmlBody = """
                <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                                  xmlns:ns="http://security.service.hub.roneps.minecofin.rw"
                                  xmlns:xsd="http://bank.vo.hub.roneps.minecofin.rw/xsd">
                    <soapenv:Header/>
                    <soapenv:Body>
                        <ns:getContractInformation>
                            <ns:contractInfoRequest>
                                <xsd:id>%s</xsd:id>
                                <xsd:password>%s</xsd:password>
                                <xsd:contractRefName>%s</xsd:contractRefName>
                                <xsd:contractRefNumber>%s</xsd:contractRefNumber>
                            </ns:contractInfoRequest>
                        </ns:getContractInformation>
                    </soapenv:Body>
                </soapenv:Envelope>
                """.formatted(
                props.getId(),
                props.getPassword(),
                contractRefName,
                contractRefNumber
        );

        return webClient.post()
                .bodyValue(xmlBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
