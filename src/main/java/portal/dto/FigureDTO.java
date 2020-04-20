package portal.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public interface FigureDTO {
    String getType();
}
