package SHOP.SHOP.model;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration

@ConfigurationProperties(prefix = "soap.guarantee")
public class CConfigProperties {
    private String url;
    private String id;
    private String password;

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
