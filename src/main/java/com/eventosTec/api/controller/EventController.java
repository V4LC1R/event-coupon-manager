package com.eventosTec.api.controller;

import com.eventosTec.api.domain.event.Event;
import com.eventosTec.api.domain.event.EventRequestDTO;
import com.eventosTec.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<Event> create(
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("date") Long date,
            @RequestParam("city") String city,
            @RequestParam("state") String uf,
            @RequestParam("remote") Boolean remote,
            @RequestParam("eventUrl") String eventUrl,
            @RequestParam(value = "image",required = false) MultipartFile image
    )
    {
        EventRequestDTO eventDto = new EventRequestDTO(
                title,
                description,
                date,
                city,
                uf,
                remote,
                eventUrl,
                image
        );

        Event newEvent = eventService.createEvent(eventDto);
        return ResponseEntity.ok(newEvent);
    }
}
