package com.br.project_crud_jdbc.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Pokemon {
    Integer id;
    String name;
    String type;
    Trainer trainer;
}
