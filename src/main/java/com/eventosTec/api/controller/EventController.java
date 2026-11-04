package com.eventosTec.api.controller;

import com.eventosTec.api.domain.event.Event;
import com.eventosTec.api.domain.event.EventRequestDTO;
import com.eventosTec.api.domain.event.EventResponseDTO;
import com.eventosTec.api.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

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

    @GetMapping()
    public ResponseEntity<List<EventResponseDTO>> getEvents(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size",defaultValue = "15") int size
    ){
        List<EventResponseDTO> list = eventService.getUpcomingEvents(page,size);

        return ResponseEntity.ok(list);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<EventResponseDTO>> filteredEvents(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String uf,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date endDate
    ){
        List<EventResponseDTO> list = eventService.getFilteredEvents(page,size,title,city,uf,startDate,endDate);

        return ResponseEntity.ok(list);
    }
}
