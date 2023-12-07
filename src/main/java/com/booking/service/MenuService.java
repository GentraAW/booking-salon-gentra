package com.booking.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class MenuService {
    public static List<Reservation> historyReservationList = new ArrayList<>();
    public static List<Reservation> reservationList = new ArrayList<>();
    static Scanner input = new Scanner(System.in);

    public static void mainMenu() {
        String[] mainMenuArr = { "Show Data", "Create Reservation", "Complete/cancel reservation", "Exit" };
        String[] subMenuArr = { "Recent Reservation", "Show Customer", "Show Available Employee",
                "Show History Reservation + Total Keuntungan", "Back to main menu" };

        int optionMainMenu;
        int optionSubMenu;

        boolean backToMainMenu = false;
        boolean backToSubMenu = false;
        do {
            PrintService.printMenu("Main Menu", mainMenuArr);
            optionMainMenu = Integer.valueOf(input.nextLine());
            switch (optionMainMenu) {
                case 1:
                    do {
                        PrintService.printMenu("Show Data", subMenuArr);
                        optionSubMenu = Integer.valueOf(input.nextLine());
                        switch (optionSubMenu) {
                            case 1:
                                PrintService.showRecentReservation(reservationList);
                                break;
                            case 2:
                                PrintService.showAllCustomer();
                                break;
                            case 3:
                                PrintService.showAllEmployee();
                                break;
                            case 4:
                                PrintService.showHistoryReservation(historyReservationList);
                                break;
                            case 0:
                                backToSubMenu = true;
                        }
                    } while (!backToSubMenu);
                    break;
                case 2:
                    // ReservationService.createReservation();
                    createReservation();
                    break;
                case 3:
                    // Add code for completing/canceling reservation
                    // ReservationService.finishAndCanceled();
                    finishAndCanceled();
                    break;
                case 0:
                    System.out.println("Exit from program. Goodbye!");
                    backToMainMenu = false;
                    break;
            }
        } while (optionMainMenu != 0);
    }

    public static void createReservation() {
        System.out.print("Enter Customer ID: ");
        String customerId = input.nextLine();

        System.out.print("Enter Employee ID: ");
        String employeeId = input.nextLine();

        System.out.print("Enter Service ID: ");
        String serviceId = input.nextLine();

        // Fetch customer, employee, and service objects berdasarkan ID dari
        // repositories
        Person customer = PersonRepository.getPersonById(customerId);
        Person employee = PersonRepository.getPersonById(employeeId);
        Service service = ServiceRepository.getServiceById(serviceId);

        if (customer instanceof com.booking.models.Customer
                && employee instanceof com.booking.models.Employee && service != null) {
            // buat reservasi baru
            Reservation reservation = new Reservation((com.booking.models.Customer) customer,
                    (com.booking.models.Employee) employee, new ArrayList<>(Arrays.asList(service)), "In Process");

            // add reservasi kedalam list
            reservationList.add(reservation);

            System.out.println("Reservation created successfully!");
            System.out.println("Reservation ID: " + reservation.getReservationId()); // menampilkan id yang dibuat

            // tampilkan semua list reservasi terbaru
            PrintService.showRecentReservation(reservationList);
        } else {
            System.out.println("Invalid customer, employee, or service ID. Unable to create reservation.");
        }
    }

    public static void finishAndCanceled() {
        // tampilkan semua list reservasi terbaru
        PrintService.showRecentReservation(reservationList);

        // opsi cancel atau finish
        System.out.println("Enter action for reservation:");
        System.out.println("1. Finish");
        System.out.println("2. Cancel");
        System.out.println("0. Back to main menu");

        int actionOption = Integer.valueOf(input.nextLine());

        switch (actionOption) {
            case 1:
                // finish reservasi
                finishReservation();
                break;
            case 2:
                // Cancel reservasi
                cancelReservation();
                break;
            case 0:
                // Back to main menu
                break;
            default:
                System.out.println("Invalid option.");
                break;
        }
    }

    private static void finishReservation() {
        System.out.print("Enter Reservation ID to finish: ");
        String reservationIdToFinish = input.nextLine();

        // cari reservasi pada list
        Reservation reservationToFinish = findReservationById(reservationIdToFinish);

        if (reservationToFinish != null && isReservationPending(reservationToFinish)) {
            // cari reservasi pada list "Finish"
            reservationToFinish.setWorkstage("Finish");
            System.out.println("Reservation dengan id " + reservationIdToFinish + " sudah Finish");

            // menghapus pada list reservasi
            reservationList.remove(reservationToFinish);

            // tambahkan status finish pada history reservasi
            historyReservationList.add(reservationToFinish);
        } else {
            System.out.println("Invalid or already finished/ canceled reservation ID.");
        }
    }

    private static void cancelReservation() {
        System.out.print("Enter Reservation ID to cancel: ");
        String reservationIdToCancel = input.nextLine();

        // cari reservasi pada list
        Reservation reservationToCancel = findReservationById(reservationIdToCancel);

        if (reservationToCancel != null && isReservationPending(reservationToCancel)) {
            // cari reservasi pada list "Canceled"
            reservationToCancel.setWorkstage("Canceled");
            System.out.println("Reservation dengan id " + reservationIdToCancel + " sudah Cancel");

            // menghapus pada list reservasi
            reservationList.remove(reservationToCancel);

            // tambahkan status finish pada history reservasi
            historyReservationList.add(reservationToCancel);
        } else {
            System.out.println("Invalid or already finished/canceled reservation ID.");
        }
    }

    private static boolean isReservationPending(Reservation reservation) {
        // Check reservasi (Waiting atau In Process)
        return "Waiting".equalsIgnoreCase(reservation.getWorkstage())
                || "In Process".equalsIgnoreCase(reservation.getWorkstage());
    }

    private static Reservation findReservationById(String reservationId) {
        for (Reservation reservation : reservationList) {
            if (reservation.getReservationId().equals(reservationId)) {
                return reservation;
            }
        }
        return null;
    }

}
