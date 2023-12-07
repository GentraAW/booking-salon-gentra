package com.booking.service;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;

public class PrintService {
        public static void printMenu(String title, String[] menuArr) {
                int num = 1;
                System.out.println(title);
                for (int i = 0; i < menuArr.length; i++) {
                        if (i == (menuArr.length - 1)) {
                                num = 0;
                        }
                        System.out.println(num + ". " + menuArr[i]);
                        num++;
                }
        }

        public static String printServices(List<Service> serviceList) {
                StringBuilder result = new StringBuilder();
                for (Service service : serviceList) {
                        result.append(service.getServiceName())
                                        .append(" (ID: ").append(service.getServiceId())
                                        .append(", Cost: ").append(service.getPrice())
                                        .append("), ");
                }
                return result.toString();
        }

        public static void showRecentReservation(List<Reservation> reservationList) {
                System.out
                                .println(
                                                "|=====================================================================================================================|");
                int num = 1;
                System.out.printf("| %-4s | %-7s | %-15s | %-49s | %-15s | %-10s |\n",
                                "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Workstage");
                System.out
                                .println(
                                                "|=====================================================================================================================|");

                for (Reservation reservation : reservationList) {
                        // Hitung tital cost

                        double totalServiceCost = reservation.getServices().stream()
                                        .mapToDouble(Service::getPrice)
                                        .sum();

                        // Mnemapilkan detail reservasi
                        System.out.printf("| %-4s | %-7s | %-15s | %-49s | %-15.2f | %-10s |\n",
                                        num, reservation.getReservationId(),
                                        reservation.getCustomer().getName(),
                                        printServices(reservation.getServices()), totalServiceCost,
                                        reservation.getWorkstage());
                        num++;

                }
                System.out
                                .println(
                                                "|=====================================================================================================================|");
        }

        public static void showAllCustomer() {

                List<Person> listAllPersons = PersonRepository.getAllPerson();
                List<Customer> listCustomers = listAllPersons.stream()
                                .filter(person -> person instanceof Customer)
                                .map(customer -> (Customer) customer)
                                .collect(Collectors.toList());

                System.out.println("|====================================================================|");
                System.out.printf("| %-7s | %-8s | %-10s | %-14s | %-15s |\n", "ID", "Name", "Address", "Membership",
                                "Wallet");
                System.out.println("|====================================================================|");

                listCustomers.forEach(customer -> {
                        String formattedWallet = String.format("Rp.%,.2f", customer.getWallet());
                        System.out.printf("| %-7s | %-8s | %-10s | %-14s | %-15s |\n",
                                        customer.getId(),
                                        customer.getName(),
                                        customer.getAddress(),
                                        customer.getMember().getMembershipName(),
                                        formattedWallet);
                });
                System.out.println("|====================================================================|");
        }

        public static void showAllEmployee() {
                List<Person> listAllPersons = PersonRepository.getAllPerson();
                List<Employee> allEmployees = listAllPersons.stream()
                                .filter(person -> person instanceof Employee)
                                .map(employee -> (Employee) employee)
                                .collect(Collectors.toList());

                System.out.println("|========================================================|");
                System.out.printf("| %-7s | %-10s | %-15s | %-13s |\n", "ID", "Name", "Address", "Experience");
                System.out.println("|========================================================|");

                // Menampilkan employee
                allEmployees.forEach(employee -> {
                        System.out.printf("| %-7s | %-10s | %-15s | %-13s |\n",
                                        employee.getId(),
                                        employee.getName(),
                                        employee.getAddress(),
                                        employee.getExperience());
                });
                System.out.println("|========================================================|");
        }

        public static void showHistoryReservation(List<Reservation> historyReservationList) {
                DecimalFormat df = new DecimalFormat("#,000.00");
                System.out
                                .println(
                                                "|=====================================================================================================================|");
                int num = 1;
                System.out.printf("| %-4s | %-7s | %-15s | %-49s | %-15s | %-10s |\n",
                                "No.", "ID", "Nama Customer", "Service", "Biaya Service", "Status");
                System.out
                                .println(
                                                "|=====================================================================================================================|");

                for (Reservation reservation : historyReservationList) {
                        double totalServiceCost;

                        // cek jika reservasi cancel
                        if (reservation.getWorkstage().equalsIgnoreCase("Canceled")) {
                                totalServiceCost = 0; // jika cancel maka cost ubah menjadi 0
                        } else {
                                // hitung total cost service
                                totalServiceCost = reservation.getServices().stream()
                                                .mapToDouble(Service::getPrice)
                                                .sum();
                        }

                        // menampilkan reservasi detail
                        System.out.printf("| %-4s | %-7s | %-15s | %-49s | %-15.2f | %-10s |\n",
                                        num, reservation.getReservationId(), reservation.getCustomer().getName(),
                                        printServices(reservation.getServices()), totalServiceCost,
                                        reservation.getWorkstage());
                        num++;
                }
                System.out
                                .println(
                                                "|=====================================================================================================================|");
                // menampilkan total cost
                double totalFinishedCost = calculateTotalFinishedCost(historyReservationList);
                System.out.println("Total Keuntungan Rp." + df.format(totalFinishedCost));
        }

        private static double calculateTotalFinishedCost(List<Reservation> historyReservationList) {
                // hitung total cost yang berstatus finish
                return historyReservationList.stream()
                                .filter(reservation -> reservation.getWorkstage().equalsIgnoreCase("Finish"))
                                .flatMapToDouble(reservation -> reservation.getServices().stream()
                                                .mapToDouble(Service::getPrice))
                                .sum();
        }
}
