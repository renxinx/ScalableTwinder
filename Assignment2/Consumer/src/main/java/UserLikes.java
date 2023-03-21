import java.util.concurrent.atomic.AtomicInteger;

public class UserLikes {
    private int numLikes;
    private int numDislikes;

    public UserLikes() {
        this.numLikes = 0;
        this.numDislikes = 0;
    }

    public int getNumLikes() {
        return numLikes;
    }

//    public void int numLikes {
//        this.numLikes = numLikes;
//    }

    public int getNumDislikes() {
        return numDislikes;
    }

//    public void int(AtomicInteger numDislikes) {
//        this.numDislikes = numDislikes;
//    }

    public void incrementLikes(){
        this.numLikes += 1;
    }
    public void incrementDislikes(){
        this.numDislikes += 1;
    }
}
