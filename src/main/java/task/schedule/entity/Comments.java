package task.schedule.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Comments extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedules schedule;

    public Comments(Users user, Schedules schedule, String content) {
        this.user = user;
        this.schedule = schedule;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
