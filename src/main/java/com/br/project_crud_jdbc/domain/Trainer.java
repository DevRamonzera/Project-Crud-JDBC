package com.br.project_crud_jdbc.domain;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Trainer {
    Integer id;
    String name;
}
