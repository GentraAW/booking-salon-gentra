package com.booking.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.booking.models.Service;

public class ServiceRepository {

    private static List<Service> serviceList = new ArrayList<>();
    static {
        Service service1 = new Service("Serv-01", "Potong Rambut", 75000);
        Service service2 = new Service("Serv-02", "Styling Rambut", 125000);
        Service service3 = new Service("Serv-03", "Pewarnaan Rambut", 500000);
        Service service4 = new Service("Serv-04", "Rebonding", 60000);
        Service service5 = new Service("Serv-05", "Mengeriting Rambut", 70000);

        serviceList.addAll(Arrays.asList(service1, service2, service3, service4, service5));

    }

    public static List<Service> getAllService() {
        return serviceList;
    }

    public static Service getServiceById(String serviceId) {
        for (Service service : serviceList) {
            if (service.getServiceId().equals(serviceId)) {
                return service;
            }
        }
        return null; // Return null if the service with the given ID is not found
    }
}
