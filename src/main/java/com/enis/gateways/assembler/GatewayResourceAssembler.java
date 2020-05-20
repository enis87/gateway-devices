package com.enis.gateways.assembler;

import com.enis.gateways.controller.GatewayController;
import com.enis.gateways.model.Gateway;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GatewayResourceAssembler implements RepresentationModelAssembler<Gateway, EntityModel<Gateway>> {

    @Override
    public EntityModel<Gateway> toModel(Gateway entity) {
        EntityModel<Gateway> getewayModel = new EntityModel<Gateway>(entity,
                linkTo(methodOn(GatewayController.class).one(entity.getSerial())).withSelfRel(),
                linkTo(methodOn(GatewayController.class).all()).withRel("gateways")
        );

        return getewayModel;
    }

}
