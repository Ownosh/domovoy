package ru.domovoy.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "building_id")
    private Long buildingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complex_id", nullable = false)
    private ResidentialComplex complex;

    @Column(name = "building_number", length = 50)
    private String buildingNumber;

    @Column(name = "floors")
    private Integer floors;

    @Column(name = "total_apartments")
    private Integer totalApartments;

    @Column(name = "year_built")
    private Integer yearBuilt;

    @Column(name = "construction_type", length = 100)
    private String constructionType;

    @Column(name = "technical_info", columnDefinition = "TEXT")
    private String technicalInfo;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relationships
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BuildingPhoto> buildingPhotos;

    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserVerification> userVerifications;
}











