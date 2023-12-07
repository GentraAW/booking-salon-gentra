package com.booking.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Reservation {
    private static int reservationCounter = 1;

    private String reservationId;
    private Customer customer;
    private Employee employee;
    private List<Service> services;
    private double reservationPrice;
    private String workstage;

    public Reservation(Customer customer, Employee employee, List<Service> services, String workstage) {
        this.reservationId = generateReservationId();
        this.customer = customer;
        this.employee = employee;
        this.services = services;
        this.reservationPrice = calculateReservationPrice();
        this.workstage = workstage;
    }

    private String generateReservationId() {
        String formattedCounter = String.format("%02d", reservationCounter++);
        return "Rsv-" + formattedCounter;
    }

    private double calculateReservationPrice() {
        return 0; // Your price calculation logic
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Reservation other = (Reservation) obj;
        return reservationId.equals(other.reservationId);
    }

    @Override
    public int hashCode() {
        return reservationId.hashCode();
    }
}
