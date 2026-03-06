package gym.domain;

import java.time.LocalDateTime;
import java.io.Serializable;

public class Session implements Identifiable<Integer>, Serializable {
    private Integer id;
    private Integer clientId;
    private LocalDateTime dateTime;
    private String description;

    public Session(Integer id, Integer clientId, LocalDateTime dateTime, String description) {
        this.id = id;
        this.clientId = clientId;
        this.dateTime = dateTime;
        this.description = description;
    }

    @Override
    public Integer getId() { return id; }

    @Override
    public void setId(Integer id) { this.id = id; }

    public Integer getClientId() { return clientId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public String getDescription() { return description; }

    public void setClientId(Integer clientId) { this.clientId = clientId; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                '}';
    }
}
