package com.br.framework.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    private String name;
    private Set<PhotoDTO> photoDTOSet;
}
