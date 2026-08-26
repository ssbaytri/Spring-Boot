package fr._42.cinema.dto;

public class SessionSearchResultDTO {

    private final Long id;
    private final String dateTime;
    private final FilmDTO film;

    public SessionSearchResultDTO(Long id, String dateTime, FilmDTO film) {
        this.id = id;
        this.dateTime = dateTime;
        this.film = film;
    }

    public Long getId() {
        return id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public FilmDTO getFilm() {
        return film;
    }

    public static class FilmDTO {

        private final String name;
        private final String posterUrl;

        public FilmDTO(String name, String posterUrl) {
            this.name = name;
            this.posterUrl = posterUrl;
        }

        public String getName() {
            return name;
        }

        public String getPosterUrl() {
            return posterUrl;
        }
    }
}