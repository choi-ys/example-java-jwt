package io.example.jwt.controller;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.IanaLinkRelations.INDEX;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * @author : choi-ys
 * @date : 2021/09/02 9:40 오전
 */
@RestController
@RequestMapping(
        value = "index",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
)
public class IndexController {

    @GetMapping
    public RepresentationModel index() {
        var indexRepresentationModel = new RepresentationModel<>();
        return indexRepresentationModel.add(linkTo(IndexController.class).withRel(INDEX));
    }
}
