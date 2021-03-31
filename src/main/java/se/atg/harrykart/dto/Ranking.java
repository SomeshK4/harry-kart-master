package se.atg.harrykart.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Ranking{
    private Integer position;
    private String horse;
    @JsonIgnore
    private Double timeTaken;

}
