package com.enis.gateways.controller;

import com.enis.gateways.assembler.GatewayResourceAssembler;
import com.enis.gateways.exceptions.DeviceNotFoundException;
import com.enis.gateways.exceptions.GatewayNotFoundException;
import com.enis.gateways.exceptions.InvalidIpAddressException;
import com.enis.gateways.exceptions.NoMoreDeviceAllowedException;
import com.enis.gateways.model.Device;
import com.enis.gateways.model.Gateway;
import com.enis.gateways.repository.GatewayRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class GatewayController {

    private final GatewayRepository repository;
    private final GatewayResourceAssembler assembler;

    public GatewayController(GatewayRepository repository, GatewayResourceAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    //Get all Gateways
    @GetMapping("/gateways")
    public CollectionModel<EntityModel<Gateway>> all() {

        List<EntityModel<Gateway>> gatewayList = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return new CollectionModel<>(gatewayList,
                linkTo(methodOn(GatewayController.class).all()).withSelfRel());
    }

    //Get one Gateway
    @GetMapping("/gateways/{serial}")
    public EntityModel<Gateway> one(@PathVariable String serial) {
        Gateway gateway = repository.findById(serial)
                .orElseThrow(() -> new GatewayNotFoundException(serial));

        return assembler.toModel(gateway);
    }

    //Create a new Gateway
    @PostMapping("/gateways")
    public ResponseEntity<?> newGateway(@RequestBody Gateway newGateway) throws URISyntaxException {
        if (!isValidIP(newGateway.getIp())) throw new InvalidIpAddressException(newGateway.getIp());

        EntityModel<Gateway> resource = assembler.toModel(repository.save(newGateway));
        repository.save(newGateway);

        return ResponseEntity
                .created(linkTo(methodOn(GatewayController.class).one(resource.getContent().getSerial())).toUri())
                .body(assembler.toModel(resource.getContent()));
    }

    //Add device
    @PostMapping("/gateways/{serial}/device")
    public ResponseEntity<?> addDevice(@RequestBody Device newDevice, @PathVariable String serial) throws URISyntaxException {
        EntityModel<Gateway> resource = assembler.toModel(repository.findById(serial)
                .map(gateway -> {
                    if (gateway.getDevices().size() == 10) {
                        throw new NoMoreDeviceAllowedException();
                    }
                    Device device = new Device(newDevice.getVendor(), newDevice.getStatus());
                    device.setGateway(gateway);
                    gateway.getDevices().add(device);

                    return repository.save(gateway);
                }).orElseThrow(() -> new GatewayNotFoundException(serial)));

        return ResponseEntity
                .created(linkTo(methodOn(GatewayController.class).one(resource.getContent().getSerial())).toUri())
                .body(assembler.toModel(resource.getContent()));
    }

    //Delete
    @DeleteMapping("/gateways/{serial}")
    public ResponseEntity<?> deleteGateway(@PathVariable String serial) {
        repository.deleteById(serial);

      //  return ResponseEntity.noContent().build();
        return ResponseEntity.ok(HttpMessage.class);
    }

    //Delete device
    @DeleteMapping("/gateways/{serial}/device/{uid}")
    public ResponseEntity<?> deleteGateway(@PathVariable String serial, @PathVariable Long uid) {
        repository.findById(serial)
                .map(gateway -> {
                    boolean removed = false;
                    for (int i = 0; i < gateway.getDevices().size(); i++) {
                        Device device = gateway.getDevices().get(i);
                        if (device.getUid().equals(uid)) {
                            gateway.getDevices().remove(i);
                            removed = true;
                            device.setGateway(null);
                            break;
                        }
                    }
                    if (!removed) throw new DeviceNotFoundException(uid);
                    return repository.save(gateway);
                }).orElseThrow(() -> new GatewayNotFoundException(serial));

        return ResponseEntity.noContent().build();
    }

    //Utils
    private boolean isValidIP(String ip) {
        Pattern pattern = Pattern.compile("^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$");
        Matcher matcher = pattern.matcher(ip);
        return matcher.find();
    }
}
