package ru.dipech.resumes.model;

import ru.dipech.resumes.util.LocalDateXmlAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@XmlAccessorType(XmlAccessType.FIELD)
public class Experience implements Serializable {

    public static final LocalDate NOW = LocalDate.of(9999, 1, 1);
    private static final long serialVersionUID = 1L;
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate dateFrom;
    @XmlJavaTypeAdapter(LocalDateXmlAdapter.class)
    private LocalDate dateTo;
    private String title;
    private String text;

    public Experience() {
    }

    public Experience(LocalDate dateFrom, LocalDate dateTo, String title) {
        this(dateFrom, dateTo, title, null);
    }

    public Experience(LocalDate dateFrom, LocalDate dateTo, String title, String text) {
        Objects.requireNonNull(dateFrom, "dateFrom must not be null");
        Objects.requireNonNull(dateTo, "dateTo must not be null");
        Objects.requireNonNull(title, "title must not be null");
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.title = title;
        this.text = text;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDate getNOW() {
        return NOW;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Experience that = (Experience) o;
        return Objects.equals(dateFrom, that.dateFrom) &&
                Objects.equals(dateTo, that.dateTo) &&
                Objects.equals(title, that.title) &&
                Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateFrom, dateTo, title, text);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MM/yyyy");
        sb.append(dateFrom.format(dateTimeFormatter));
        sb.append(" - ");
        if (!dateTo.equals(NOW)) {
            sb.append(dateTo.format(dateTimeFormatter));
        } else {
            sb.append("Сейчас");
        }
        sb.append(" | ");
        sb.append(title);
        if (text != null) {
            sb.append(". ").append(text);
        }
        sb.append("\n");
        return sb.toString();
    }

}
