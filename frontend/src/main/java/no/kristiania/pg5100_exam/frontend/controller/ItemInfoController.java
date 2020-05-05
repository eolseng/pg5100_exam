package no.kristiania.pg5100_exam.frontend.controller;

import no.kristiania.pg5100_exam.backend.entity.Trip;
import no.kristiania.pg5100_exam.backend.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import java.util.List;

@Named
@RequestScoped
public class ItemInfoController {

    @Autowired
    TripService itemService;

    public Trip getItem(Long id) {
        return itemService.getTrip(id, false);
    }

    public List<Trip> getAllItems() {
        return itemService.getAllTrips(false);
    }

}
