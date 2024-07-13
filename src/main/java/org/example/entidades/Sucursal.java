package org.example.entidades;


import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalTime;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "empresa")  // Excluir empresa para evitar recursión infinita
@SuperBuilder
public class Sucursal extends Base {
    private Long id;
    private String nombre;
    private LocalTime horarioApertura;
    private LocalTime horarioCierre;
    private boolean esCasaMatriz;

    @OneToOne
    private Domicilio domicilio;

    @ManyToOne
    private Empresa empresa;
}

