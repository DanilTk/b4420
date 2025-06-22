package com.example.shared.infrastructure;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import static lombok.AccessLevel.PROTECTED;

@MappedSuperclass
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
@SuperBuilder
public abstract class BaseEntity implements Serializable {

    @Id
    @Column(name = "uuid", nullable = false)
    @NonNull
    private UUID uuid;

    @Version
    @Column(name = "version", nullable = false)
    @Setter
    private Integer version;

    @Column(name = "created_at", nullable = false)
    @Setter
    @NonNull
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    @UpdateTimestamp
    @Setter
    @NonNull
    private Instant updatedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BaseEntity that)) return false;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
