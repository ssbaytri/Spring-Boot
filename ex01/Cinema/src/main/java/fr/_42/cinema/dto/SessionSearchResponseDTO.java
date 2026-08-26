package fr._42.cinema.dto;

import java.util.List;

public class SessionSearchResponseDTO {

    private final List<SessionSearchResultDTO> sessions;

    public SessionSearchResponseDTO(List<SessionSearchResultDTO> sessions) {
        this.sessions = sessions;
    }

    public List<SessionSearchResultDTO> getSessions() {
        return sessions;
    }
}