package mju.capstone.project.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.project.response.Response;
import mju.capstone.project.service.DummyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DummyController {

    private final DummyService dummyService;

    @GetMapping("/all")
    public Response findAll() {
        return Response.success(dummyService.getList());
    }

    @GetMapping("/{id}")
    public Response findOne(@PathVariable Long id) {
        return Response.success(dummyService.getDummy(id));
    }
}
