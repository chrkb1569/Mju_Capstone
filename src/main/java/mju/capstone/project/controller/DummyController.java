package mju.capstone.project.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.DummyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DummyController {

    private final DummyService dummyService;

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Response findAll() {
        return Response.success(dummyService.getList());
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Response findOne(@PathVariable Long id) {
        return Response.success(dummyService.getDummy(id));
    }
}
