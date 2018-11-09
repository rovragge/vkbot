package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "transitions")
@Getter
@Setter
public class Transitions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="state_id")
    private DialogStateDB stateDB ;

    @Column(name = "regexp")
    private String regexp;

    @ManyToOne
    @JoinColumn(name="target_state_id")
    private DialogStateDB targetStateDB ;
}
