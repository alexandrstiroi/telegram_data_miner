package org.shtiroy_ap.telegram.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

public class Enquiry {
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime date;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateAnswered;
    private String title;
    private String description;
    private String answer;

    public Enquiry() {
    }

    public Enquiry(LocalDateTime date, LocalDateTime dateAnswered, String title, String description, String answer) {
        this.date = date;
        this.dateAnswered = dateAnswered;
        this.title = title;
        this.description = description;
        this.answer = answer;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public LocalDateTime getDateAnswered() {
        return dateAnswered;
    }

    public void setDateAnswered(LocalDateTime dateAnswered) {
        this.dateAnswered = dateAnswered;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(this.title)){
            sb.append("<i>").append(this.title).append("</i>\n");
        }
        if (StringUtils.hasText(this.description)){
            sb.append(this.description).append("\n");
        }
        if (StringUtils.hasText(this.answer)){
            sb.append(this.answer).append("\n");
        }
        return sb.toString();
    }
}
