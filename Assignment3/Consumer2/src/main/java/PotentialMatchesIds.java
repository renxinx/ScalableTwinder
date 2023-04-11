import java.util.ArrayList;
import java.util.List;

public class PotentialMatchesIds {
    private final List<String> potentialMatchesIds;


    public PotentialMatchesIds() {
        this.potentialMatchesIds = new ArrayList<>();
    }

    public void addRightId(String swipee){
        this.potentialMatchesIds.add(swipee);
    }

    public List<String> getTopPotentialMatchesIds(){
        int upperBound = Math.min(100, this.potentialMatchesIds.size());
        ArrayList<String> topPotentialMatchesIds = new ArrayList<>();
        for (int i = 0;i<upperBound;i++){
            topPotentialMatchesIds.add(this.potentialMatchesIds.get(i));
        }
        return topPotentialMatchesIds;
    }
}
