public class Swipe {
    String swiper;
    String swipee;
    String comment;
    String leftOrRight;

    public Swipe(String swiper, String swipee, String comment, String leftOrRight) {
        this.swiper = swiper;
        this.swipee = swipee;
        this.comment = comment;
        this.leftOrRight = leftOrRight;
    }

    public String getSwiper() {
        return swiper;
    }

    public void setSwiper(String swiper) {
        this.swiper = swiper;
    }

    public String getSwipee() {
        return swipee;
    }

    public void setSwipee(String swipee) {
        this.swipee = swipee;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLeftOrRight() {
        return leftOrRight;
    }

    public void setLeftOrRight(String leftOrRight) {
        this.leftOrRight = leftOrRight;
    }
}
