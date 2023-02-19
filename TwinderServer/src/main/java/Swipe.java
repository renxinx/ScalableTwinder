public class Swipe {
    Integer swiper;
    Integer swipee;
    String comment;

    public Swipe(Integer swiper, Integer swipee, String comment) {
        this.swiper = swiper;
        this.swipee = swipee;
        this.comment = comment;
    }

    public Integer getSwiper() {
        return swiper;
    }

    public void setSwiper(Integer swiper) {
        this.swiper = swiper;
    }

    public Integer getSwipee() {
        return swipee;
    }

    public void setSwipee(Integer swipee) {
        this.swipee = swipee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
