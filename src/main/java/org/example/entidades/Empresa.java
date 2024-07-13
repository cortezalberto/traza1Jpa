package org.example.entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@SuperBuilder
public class Empresa extends  Base{

    private String nombre;
    private String razonSocial;
    @Column(unique = true)
    private Long cuil;
    private String logo;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "empresa")
    @Builder.Default
    private Set<Sucursal> sucursales = new HashSet<>();



}
