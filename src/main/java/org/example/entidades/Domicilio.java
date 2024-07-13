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
@ToString(exclude = "localidad")  // Excluir localidad para evitar recursión infinita
@SuperBuilder



public class Domicilio extends Base{
    private String calle;
    private Integer numero;
    private Integer cp;
    private Integer piso;
    private Integer nroDpto;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "localidad_id")

    private Localidad localidad;


}
