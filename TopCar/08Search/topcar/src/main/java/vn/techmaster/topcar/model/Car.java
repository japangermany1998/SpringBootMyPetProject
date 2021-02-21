package vn.techmaster.topcar.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @JsonIgnore
    int id;

    String model;
    String manufacture;
    int price;
    int sale;
    String photo;

    public boolean matchWithKeyword(String keyword) {
        String keywordLowerCase = keyword.toLowerCase();
        return (manufacture.toLowerCase().contains(keywordLowerCase) ||
                model.toLowerCase().contains(keywordLowerCase));
    }
}
