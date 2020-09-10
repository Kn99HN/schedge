package scraping.models;

public final class Review {
    public final String comment;

    public Review(String comment) {
        this.comment = comment;
    }

    public String toString() {
        return this.comment;
    }
}