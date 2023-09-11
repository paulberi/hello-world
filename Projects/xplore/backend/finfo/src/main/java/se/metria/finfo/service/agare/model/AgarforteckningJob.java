package se.metria.finfo.service.agare.model;

import lombok.Data;

import java.util.List;

@Data
public class AgarforteckningJob {
    private int id;

    private String status;

    private List<String> fastigheter;
}

