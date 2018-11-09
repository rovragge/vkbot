package model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dialogstates")
@Getter
@Setter
public class DialogState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "message")
    private String message;

    @Column(name = "keyboard")
    private String keyboard;

    @OneToMany(mappedBy = "stateDB")
    private List<Transitions> transitions;

}
