package com.raven.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@SQLDelete(sql = "UPDATE books SET is_active = FALSE WHERE id = ?")
@SQLRestriction("is_active = TRUE")
@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created", nullable = false)
    private Long created;

    @Column(name = "updated", nullable = false)
    private Long updated;

    @Column(name = "full_name", nullable = false)
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;


    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must be less than 100 characters")
    private String email;

    @Column(name = "phone", nullable = true)
    @Pattern(regexp = "^\\+\\d{6,14}$", message = "Phone must be between 6 and 14 digits and start with +")
    private String phone;

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @PrePersist
    protected void onCreate() {
        long currentTimeMillis = System.currentTimeMillis();
        this.created = currentTimeMillis;
        this.updated = currentTimeMillis;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updated = System.currentTimeMillis();
    }
}
