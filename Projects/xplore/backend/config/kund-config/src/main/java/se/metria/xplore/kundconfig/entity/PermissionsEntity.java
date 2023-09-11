package se.metria.xplore.kundconfig.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "permissions")
public class PermissionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    UserEntity user;

    String roll;

    String produkt;

    @ManyToOne
    @JoinColumn(name = "kund_id")
    KundEntity kund;
}
