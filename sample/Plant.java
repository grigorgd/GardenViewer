package sample;

public class Plant {

    private int zoneHashCode;
    private String name;
    private String latinName;
    private String soil;
    private String reaction;
    private String position;
    private String other;

    public Plant(int zoneHashCode, String name, String latinName, String soil, String reaction, String position, String other){
        this.zoneHashCode = zoneHashCode;
        this.name = name;
        this.latinName = latinName;
        this.soil = soil;
        this.reaction = reaction;
        this.position = position;
        this.other = other;
    }

    public int getZoneHashCode() {
        return zoneHashCode;
    }
    public void setZoneHashCode(int zoneHashCode) {
        this.zoneHashCode = zoneHashCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getSoil() {
        return soil;
    }

    public void setSoil(String soil) {
        this.soil = soil;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

}
