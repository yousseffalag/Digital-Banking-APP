package org.example.dtos;

import lombok.Data;

import java.util.UUID;


@Data
public class CustomerDTO {

    private UUID id;
    private String name;
    private String email;

}
