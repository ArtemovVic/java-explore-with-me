package ru.yandex.practicum.dao.model;

import jakarta.persistence.*;
import lombok.*;
import ru.yandex.practicum.dao.model.constant.EventState;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "events")
@NamedEntityGraph(
        name = Event.ENTITY_GRAPH_USER_AND_CATEGORY,
        attributeNodes = {
                @NamedAttributeNode("user"),
                @NamedAttributeNode("category")
        }
)
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    public static final String ENTITY_GRAPH_USER_AND_CATEGORY = "event.user_and_category";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "location_lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "location_lon"))
    })
    private Location location;

    @Enumerated(EnumType.STRING)
    private EventState state = EventState.PENDING;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "publish_date")
    private LocalDateTime published;

    private LocalDateTime created;
    private LocalDateTime updated;

    private boolean paid = false;

    @Column(name = "request_moderation")
    private boolean requestModeration = false;

    @Column(name = "participant_limit")
    private Integer participantLimit = 0;

    @Column(name = "confirmed_requests")
    private Integer confirmedRequests = 0;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String annotation;

    private String description;

    @PrePersist
    protected void onCreate() {
        created = LocalDateTime.now();
        updated = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = LocalDateTime.now();
    }
}
