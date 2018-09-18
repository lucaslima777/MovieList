package eokoe.teste.developer.lln.movielist;

public class FilmEntity {

    private int Position;
    private String Title;
    private float Vote;
    private Double Popularity;
    private String Date;


    public FilmEntity(int position, String title, float vote, Double popularity, String date) {
        this.Title = title;
        this.Position = position;
        this.Vote = vote;
        this.Popularity = popularity;
        this.Date = date;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public float getVote() {
        return Vote;
    }

    public void setVote(float vote) {
        Vote = vote;
    }

    public Double getPopularity() {
        return Popularity;
    }

    public void setPopularity(Double popularity) {
        Popularity = popularity;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }


    public void setPosition(int position) {
        Position = position;
    }

    public int getPosition() {
        return Position;
    }

}
