package model;

import database.HibernateUtil;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.Session;

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

    @OneToMany(mappedBy = "dialogState")
    private List<Transitions> transitions;

    public List<Transitions> getTransitions(){
        Session session = HibernateUtil.getSessionFactory().openSession();
        Hibernate.initialize(transitions);
        session.close();
        return transitions;
    }

}
