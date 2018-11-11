package model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "dialog_states")
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

    @OneToMany(mappedBy = "dialogState", fetch = FetchType.EAGER)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Transitions> transitions;

}
