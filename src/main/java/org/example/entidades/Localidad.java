package org.example.entidades;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@SuperBuilder
public class Localidad extends Base{
    private String nombre;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "provincia_id")
    private Provincia provincia;

}
