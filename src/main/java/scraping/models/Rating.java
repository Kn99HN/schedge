package scraping.models;
import java.util.List;
import scraping.models.Review;
public final class Rating {
  public final int instructorId;
  public final Integer rmpTeacherId;
  public final float rating;
  public List<String> reviews;

  // For the sake of simplicity, forget mostHelpful for now
  public Rating(int instructorId, Integer rmpTeacherId, float rating, List<String> reviews) {
    this.instructorId = instructorId;
    this.rmpTeacherId = rmpTeacherId;
    this.rating = rating;
    this.reviews = reviews;
  }

  public void addReview(List<String> review) {
    this.reviews.addAll(review);
  }

  public String toString() {
    return "URL: " + this.rmpTeacherId + ", Rating: " + this.rating + ". Reviews:\n" + reviews.toString();
  }
}
