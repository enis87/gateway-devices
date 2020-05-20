package com.enis.gateways.model;



import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
public class Gateway {

    @Id
    @Column(unique=true)
    private String serial;

    @NotNull(message = "name can not be null.")
    @NotEmpty(message = "name can not be empty.")
    private String name;

    @NotNull(message = "ip can not be null.")
    @NotEmpty(message = "ip can not be empty.")
    private String ip;

    @OneToMany(mappedBy = "gateway", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    List<Device> devices;

    public Gateway(String serial, String name, String ip ) {
        this.serial = serial;
        this.name = name;
        this.ip = ip;
    }

    public Gateway() {
    }

    @Override
    public String toString() {
        return "Gateway{" +
                "serial='" + serial + '\'' +
                ", name='" + name + '\'' +
                ", ip='" + ip + '\'' +
                '}';
    }

}
