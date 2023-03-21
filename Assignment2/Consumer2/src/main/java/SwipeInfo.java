public class SwipeInfo {
    private String swiper;
    private String swipee;
    private String comment;
    private String leftOrRight;

    public SwipeInfo(String swiper, String swipee, String comment, String leftOrRight) {
        this.swiper = swiper;
        this.swipee = swipee;
        this.comment = comment;
        this.leftOrRight = leftOrRight;
    }

    public String getSwiper() {
        return swiper;
    }

    public void setSwiperId(String swiperId) {
        this.swiper = swiperId;
    }

    public String getSwipee() {
        return swipee;
    }

    public void setSwipeeId(String swipee) {
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
