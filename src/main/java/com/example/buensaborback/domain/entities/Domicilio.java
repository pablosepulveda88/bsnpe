package com.example.buensaborback.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@ToString
@Builder
public class Domicilio extends Base{

    private String calle;
    private Integer numero;
    private Integer cp;

    @ManyToOne
    @JoinColumn(name = "localidad_id")
    private Localidad localidad;

    @ManyToMany(mappedBy = "domicilios")
    //SE AGREGA EL BUILDER.DEFAULT PARA QUE BUILDER NO SOBREESCRIBA LA INICIALIZACION DE LA LISTA
    @Builder.Default
    private Set<Cliente> clientes = new HashSet<>();

    @OneToOne
    private Sucursal sucursal;

    @OneToMany(mappedBy = "domicilio", fetch = FetchType.LAZY)
    private Set<Pedido> pedidos = new HashSet<>();

}
