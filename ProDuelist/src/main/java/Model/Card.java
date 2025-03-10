package Model;

public class Card {
    private int id;
    private String name;
    private String type;
    private String frameType;
    private String race;
    private String attribute;
    private int level;
    private int attack;
    private int defense;
    private String desc;
    private String imageUrl;

    public Card(int id, String name, String type, String frameType, String race, String attribute,
                int level, int attack, int defense, String desc, String imageUrl) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.frameType = frameType;
        this.race = race;
        this.attribute = attribute;
        this.level = level;
        this.attack = attack;
        this.defense = defense;
        this.desc = desc;
        this.imageUrl = imageUrl;
    }

    public boolean checkExtraMonster() {
        if (this.frameType.equals("fusion") || this.frameType.equals("xyz") || this.frameType.equals("link") || this.frameType.equals("synchro")) {
            return true;
        }
        return false;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getFrameType() { return frameType; }
    public String getRace() { return race; }
    public String getAttribute() { return attribute; }
    public int getLevel() { return level; }
    public int getAttack() { return attack; }
    public int getDefense() { return defense; }
    public String getDesc() { return desc; }
    public String getImageUrl() { return imageUrl; }
}
