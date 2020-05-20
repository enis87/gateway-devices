package com.enis.gateways.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class Device {

    @Id
    @GeneratedValue
    @Column(unique = true)
    private Long uid;//UUID uuid = UUID.randomUUID();

    @NotNull(message = "vendor can not be null.")
    @NotEmpty(message = "vendor can not be empty.")
    private String vendor;

    protected Date createdDate;

    @NotNull(message = "status can not be null.")
    private Status status;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gatewaySerial")
    private Gateway gateway;

    public Device() {
        this.createdDate = Calendar.getInstance().getTime();
    }

    public Device(String vendor, Status status) {
        this();
        this.vendor = vendor;
        this.status = status;


    }



}
