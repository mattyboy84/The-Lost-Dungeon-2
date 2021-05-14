package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Items {

    Vecc2f position;
    Random rand = new Random();
    ImageView itemImage;
    String name;
    int ID;
    int price;
    Text label;
    ImageView coinIcon;
    int i;
    int j;
    int arrayPos;
    //
    String itemType;
    String description;
    String effect;

    public Items(ArrayList<Items> items) {//normal random item rooms
        this.arrayPos = items.size();
        this.position = new Vecc2f(rand.nextInt(1540) + 150, rand.nextInt(750) + 150);
        switch (rand.nextInt(9)) {
            case 0:
            case 1:
            case 2:
                healthPotion();
                break;
            case 3:
            case 4:
            case 5:
                speedPotion();
                break;
            case 6:
            case 7:
                StrengthPotion();
                break;
            case 8:
                healthIncreasePotion();
                break;


        }
    }

    private void healthIncreasePotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile004.png");
        this.name = "Health Potion";
        this.itemType = "Consumable";
        this.description = "HEALTH";
        this.effect = "+1 heart";
        this.ID = 5;
    }

    private void StrengthPotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile062.png");
        this.name = "Strength Potion";
        this.itemType = "Consumable";
        this.description = "STRONG";
        this.effect = "Increased damage";
        this.ID = 4;

    }

    private void speedPotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile053.png");
        this.name = "Speed Potion";
        this.itemType = "Consumable";
        this.description = " An Invigorating tonic\n that energies the \n eager traveler";
        this.effect = "Increased Speed";
        this.ID = 3;

    }

    private void healthPotion() {
        this.itemImage = new ImageView("file:src\\game\\items\\potions\\tile005.png");
        this.name = "Health Potion";
        this.itemType = "Consumable";
        this.description = " A revitalizing drink to\n heal the wounds of\n weary travelers.";
        this.effect = "+2 Health";
        this.ID = 1;
    }

    public Items(Vecc2f inventoryPos, Group group, int ID, int j, int i, int RES, ImageView itemImage, String name, String itemType, String description, String effect) {//inventory items
        this.position = new Vecc2f(((inventoryPos.x + 39) + (j * RES)), ((inventoryPos.y + 39) + (i * RES)));
        this.i = i;
        this.j = j;
        //
        this.itemImage = itemImage;
        this.name = name;
        this.itemType = itemType;
        this.description = description;
        this.effect = effect;
        this.ID = ID;
    }


    public Items(int roomX, int roomY, String name, ArrayList<Items> items) {//shop items
        this.position = new Vecc2f(750 + (150 * items.size()), 500);
        switch (items.size()) {
            case 0://staff
                this.itemImage = new ImageView("file:src\\game\\items\\staff.png");
                this.price = 5;
                break;
            case 1:
                this.itemImage = new ImageView("file:src\\game\\items\\MapItem.png");
                this.price = 2;
                break;
            case 2:
                this.itemImage = new ImageView("file:src\\game\\items\\attack boost.png");
                this.price = 5;
                this.ID = 3;

                break;
        }
        this.name = name;
        this.label = new Text(String.valueOf(price));
        this.coinIcon = new ImageView("file:src\\game\\items\\Coin Icon.png");
    }


    public void check(Hero hero, Inventory inventory, ArrayList<Items> items, Group group) {
        if (hero.getHeroBodyHitbox().getBoundsInParent().intersects(this.itemImage.getBoundsInParent())) {
            group.getChildren().remove(this.itemImage);
            items.remove(this.arrayPos);
            for (Items item : items) {
                if (item.getArrayPos() > this.arrayPos) {
                    item.setArrayPos(item.getArrayPos() - 1);
                }
            }
            //System.out.println(this.name);
            inventory.addNewItem(inventory, group, this.ID, this.itemImage, this.name, this.itemType, this.description, this.effect);

        }
    }

    public void load(Group group) {
        group.getChildren().add(this.itemImage);
        this.itemImage.relocate(position.x, position.y);
        this.itemImage.setViewOrder(-2);

    }

    public void unload(Group group) {
        group.getChildren().remove(this.itemImage);
    }

    public Vecc2f getPosition() {
        return position;
    }

    public void setPosition(Vecc2f position) {
        this.position = position;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public ImageView getItemImage() {
        return itemImage;
    }

    public void setItemImage(ImageView itemImage) {
        this.itemImage = itemImage;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public void setArrayPos(int arrayPos) {
        this.arrayPos = arrayPos;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getJ() {
        return j;
    }

    public void setJ(int j) {
        this.j = j;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Text getLabel() {
        return label;
    }

    public void setLabel(Text label) {
        this.label = label;
    }

    public ImageView getCoinIcon() {
        return coinIcon;
    }

    public void setCoinIcon(ImageView coinIcon) {
        this.coinIcon = coinIcon;
    }
}
