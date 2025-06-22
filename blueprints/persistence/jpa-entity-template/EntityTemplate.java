package com.example.{{package}};

import lombok.*;
import lombok.experimental.SuperBuilder;
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "{{table_name}}")
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public class {{EntityName}}Entity extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

}
