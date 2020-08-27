package model;

import java.time.LocalDateTime;

public class ConsultantReport {
    private String title;
    private String customer;
    private LocalDateTime start;
    private LocalDateTime end;
    private String location;

    public ConsultantReport(String title, String customer, LocalDateTime start, LocalDateTime end, String location){
        this.title = title;
        this.customer = customer;
        this.start = start;
        this.end = end;
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getCustomer() {
        return customer;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }
}
