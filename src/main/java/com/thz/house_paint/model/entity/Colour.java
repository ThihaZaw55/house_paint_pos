package com.thz.house_paint.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colours")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Colour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "colour_id")
    private int colourId;

    @Column(name = "colour_code", nullable = false, length = 60)
    private String colourCode;
    
    @Column(name = "colour_name", nullable = false, length = 60)
    private String colourName;


}
