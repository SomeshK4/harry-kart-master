package se.atg.harrykart.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Response {

    private List<Ranking> ranking;
}
