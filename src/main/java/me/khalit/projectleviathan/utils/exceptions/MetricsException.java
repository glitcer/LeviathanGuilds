package me.khalit.projectleviathan.utils.exceptions;

import me.khalit.projectleviathan.Metrics;

public class MetricsException extends Exception {

    public MetricsException() {
        super("Unfortunatelly, there's a problem with gathering informations to mcstats. " +
                "If you disabled/don't want this option - ignore that.");
    }
}
