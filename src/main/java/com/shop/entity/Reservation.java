package com.shop.entity;

import com.shop.constant.ReservationStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    private LocalDate date;

    private LocalTime time;

    private boolean isDeleted;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Reservation(String name, String phone, LocalDate date, LocalTime time,
                       boolean isDeleted, ReservationStatus status, Member member) {
        this.name = name;
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.isDeleted = isDeleted;
        this.status = (status != null) ? status : ReservationStatus.PENDING;
        this.member = member;
    }

    public void updateIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public boolean updateStatus(ReservationStatus newStatus) {
        if (this.status == ReservationStatus.CANCELLED) {
            return false;
        }

        if (this.status == ReservationStatus.PENDING) {
            if (newStatus == ReservationStatus.CONFIRMED || newStatus == ReservationStatus.CANCELLED) {
                this.status = newStatus;
                return true;
            }
        } else if (this.status == ReservationStatus.CONFIRMED) {
            if (newStatus == ReservationStatus.CANCELLED) {
                this.status = newStatus;
                return true;
            }
        }

        return false;
    }
}